/*
 * Copyright 2015-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.luckykuang.auth.service.impl;

import com.luckykuang.auth.constants.enums.ErrorCode;
import com.luckykuang.auth.exception.BusinessException;
import com.luckykuang.auth.model.Dept;
import com.luckykuang.auth.model.Menu;
import com.luckykuang.auth.model.Role;
import com.luckykuang.auth.model.User;
import com.luckykuang.auth.repository.DeptRepository;
import com.luckykuang.auth.repository.RoleRepository;
import com.luckykuang.auth.repository.UserRepository;
import com.luckykuang.auth.request.UserReq;
import com.luckykuang.auth.service.UserService;
import com.luckykuang.auth.utils.AssertUtils;
import com.luckykuang.auth.utils.PageUtils;
import com.luckykuang.auth.vo.PageResultVo;
import com.luckykuang.auth.vo.PageVo;
import com.luckykuang.auth.vo.UserDetailsVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author luckykuang
 * @date 2023/4/20 14:19
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DeptRepository deptRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User addUser(UserReq userReq) {
        UserReq from = UserReq.from(userReq);
        Optional<User> userOptional = userRepository.findByUsername(from.username());
        AssertUtils.isTrue(userOptional.isEmpty(), ErrorCode.NAME_EXIST);
        return saveUser(null, from);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username.toLowerCase()).orElse(null);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public PageResultVo<User> getUserByPage(PageVo page) {
        Pageable pageable = PageUtils.getPageable(page, Sort.Direction.DESC, "updateTime");
        return PageUtils.getPageResult(userRepository.findAll(pageable));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User updateUser(Long id, UserReq userReq) {
        UserReq from = UserReq.from(userReq);
        Optional<User> repositoryById = userRepository.findById(id);
        AssertUtils.isTrue(repositoryById.isPresent(), ErrorCode.ID_NOT_EXIST);
        Optional<User> usersOptional = userRepository.findByIdIsNotAndUsername(id, from.username());
        AssertUtils.isTrue(usersOptional.isEmpty(), ErrorCode.NAME_EXIST);
        return saveUser(id, from);
    }

    @Override
    public void delUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ID_NOT_EXIST));
        userRepository.delete(user);
    }

    @Override
    public UserDetailsVo getUserDetails(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.USERNAME_NOT_EXIST));
        Set<Role> roles = Optional.ofNullable(user.getRoles())
                .orElseThrow(() -> new BusinessException(ErrorCode.ROLE_NOT_EXIST));
        Set<Long> roleIds = roles.stream().map(Role::getId).collect(Collectors.toSet());
        // 获取最大数据权限
        // data_scope 值越小，权限越大
        Integer maxDataScope = roleRepository.findMaxDataScope(roleIds);

        // 获取所有的菜单权限
        Set<Menu> menus = new HashSet<>();
        roles.forEach(role -> menus.addAll(role.getMenus()));

        UserDetailsVo userDetailsVo = new UserDetailsVo();
        userDetailsVo.setUserId(user.getId());
        userDetailsVo.setUsername(user.getUsername());
        userDetailsVo.setPassword(user.getPassword());
        userDetailsVo.setDeptId(user.getDept().getId());
        userDetailsVo.setDataScope(maxDataScope);
        userDetailsVo.setRoles(roles);
        userDetailsVo.setMenus(menus);
        return userDetailsVo;
    }

    private User saveUser(Long id, UserReq userReq) {
        Optional<User> userOptional = userRepository.findByPhone(userReq.phone());
        AssertUtils.isTrue(userOptional.isEmpty(), ErrorCode.PHONE_EXIST);
        Optional<Dept> deptOptional = deptRepository.findById(userReq.deptId());
        if (deptOptional.isEmpty()){
            throw new BusinessException(ErrorCode.DEPT_NOT_EXIST);
        }
        User user = new User();
        user.setId(id);
        user.setName(userReq.name());
        user.setUsername(userReq.username());
        // 加密
        user.setPassword(passwordEncoder.encode(userReq.password()));
        user.setGender(userReq.gender());
        user.setEmail(userReq.email());
        user.setPhone(userReq.phone());
        user.setAvatar(userReq.avatar());
        user.setStatus(userReq.status());
        
        User save = userRepository.save(user);
        save.setDept(deptOptional.get());
        return save;
    }
}
