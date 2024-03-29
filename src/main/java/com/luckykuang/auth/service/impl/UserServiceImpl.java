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

import com.luckykuang.auth.base.ApiResult;
import com.luckykuang.auth.constants.enums.ErrorCode;
import com.luckykuang.auth.exception.BusinessException;
import com.luckykuang.auth.model.primary.Dept;
import com.luckykuang.auth.model.primary.Menu;
import com.luckykuang.auth.model.primary.Role;
import com.luckykuang.auth.model.primary.User;
import com.luckykuang.auth.repository.primary.DeptRepository;
import com.luckykuang.auth.repository.primary.RoleRepository;
import com.luckykuang.auth.repository.primary.UserRepository;
import com.luckykuang.auth.service.UserService;
import com.luckykuang.auth.utils.AssertUtils;
import com.luckykuang.auth.utils.BCryptUtils;
import com.luckykuang.auth.utils.PageUtils;
import com.luckykuang.auth.utils.RequestUtils;
import com.luckykuang.auth.vo.PageResultVo;
import com.luckykuang.auth.vo.PageVo;
import com.luckykuang.auth.vo.UserDetailsVo;
import com.luckykuang.auth.vo.request.PasswordRed;
import com.luckykuang.auth.vo.request.UserReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.luckykuang.auth.constants.CoreConstants.DEFAULT_PASSWORD;

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

    @Override
//    @Transactional(rollbackFor = Exception.class)
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
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username.toLowerCase());
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
//    @Transactional(rollbackFor = Exception.class)
    public User updateUser(Long id, UserReq userReq) {
        UserReq from = UserReq.from(userReq);
        Optional<User> repositoryById = userRepository.findById(id);
        AssertUtils.isTrue(repositoryById.isPresent(), ErrorCode.ID_NOT_EXIST);
        Optional<User> usersOptional = userRepository.findByIdIsNotAndUsername(id, from.username());
        AssertUtils.isTrue(usersOptional.isEmpty(), ErrorCode.NAME_EXIST);
        return saveUser(id, from);
    }

    @Override
    public ApiResult<Void> delUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ID_NOT_EXIST));
        userRepository.delete(user);
        return ApiResult.success();
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
        userDetailsVo.setDeptId(user.getDept() == null ? null : user.getDept().getId());
        userDetailsVo.setDataScope(maxDataScope);
        userDetailsVo.setRoles(roles);
        userDetailsVo.setMenus(menus);
        return userDetailsVo;
    }

    @Override
    public ApiResult<Void> updatePass(PasswordRed passwordRed) {
        String currentPassword = RequestUtils.getPassword();
        boolean match = BCryptUtils.match(passwordRed.oldPassword(), currentPassword);
        if (!match){
            throw new BusinessException(ErrorCode.CURRENT_PASSWORD_ERROR);
        }
        if (!Objects.equals(passwordRed.newPassword(),passwordRed.confirmNewPassword())){
            throw new BusinessException(ErrorCode.NEW_AND_CONFIRM_PASSWORD_DIFFER);
        }
        Optional<User> userOptional = userRepository.findById(RequestUtils.getUserId());
        if (userOptional.isEmpty()){
            throw new BusinessException(ErrorCode.USER_NOT_EXIST);
        }
        User user = userOptional.get();
        user.setPassword(BCryptUtils.encode(passwordRed.newPassword()));
        userRepository.save(user);
        return ApiResult.success();
    }

    @Override
    public ApiResult<Void> resetPass() {
        Optional<User> userOptional = userRepository.findById(RequestUtils.getUserId());
        if (userOptional.isEmpty()){
            throw new BusinessException(ErrorCode.USER_NOT_EXIST);
        }
        User user = userOptional.get();
        user.setPassword(BCryptUtils.encode(DEFAULT_PASSWORD));
        userRepository.save(user);
        return ApiResult.success();
    }

    @Override
    public UserDetailsVo getUserInfo() {
        return getUserDetails(RequestUtils.getUsername());
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
        if (id == null) {
            // 加密
            user.setPassword(BCryptUtils.encode(DEFAULT_PASSWORD));
        }
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
