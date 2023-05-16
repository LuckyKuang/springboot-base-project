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
import com.luckykuang.auth.config.jwt.JwtTokenProvider;
import com.luckykuang.auth.constants.RedisConstants;
import com.luckykuang.auth.enums.ErrorCode;
import com.luckykuang.auth.exception.BusinessException;
import com.luckykuang.auth.model.Dept;
import com.luckykuang.auth.model.Login;
import com.luckykuang.auth.model.Role;
import com.luckykuang.auth.model.User;
import com.luckykuang.auth.record.JwtRspRec;
import com.luckykuang.auth.record.LoginRec;
import com.luckykuang.auth.record.RefreshRec;
import com.luckykuang.auth.record.UserRec;
import com.luckykuang.auth.repository.DeptRepository;
import com.luckykuang.auth.repository.RoleRepository;
import com.luckykuang.auth.repository.UserRepository;
import com.luckykuang.auth.service.UserService;
import com.luckykuang.auth.utils.AssertUtils;
import com.luckykuang.auth.utils.PageUtils;
import com.luckykuang.auth.utils.RedisUtils;
import com.luckykuang.auth.vo.PageResultVo;
import com.luckykuang.auth.vo.PageVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static com.luckykuang.auth.constants.CoreConstants.ADMIN;
import static com.luckykuang.auth.constants.CoreConstants.BEARER_HEAD;

/**
 * @author luckykuang
 * @date 2023/4/20 14:19
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Value("${app.jwt-expiration.access-token-milliseconds:10000}")
    private long jwtAccessTokenExpirationDate;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DeptRepository deptRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final RedisUtils redisUtils;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User addUser(UserRec userRec) {
        UserRec from = UserRec.from(userRec);
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
    public ApiResult<JwtRspRec> login(LoginRec loginRec) {
        LoginRec from = LoginRec.from(loginRec);
        User user = userRepository.findByUsername(from.username())
                .orElseThrow(() -> new BusinessException(ErrorCode.USERNAME_NOT_EXIST));
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                from.username(),
                from.password()
        );
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        String accessToken = jwtTokenProvider.generateAccessToken(authenticate,String.valueOf(user.getId()));
        String refreshToken = jwtTokenProvider.generateRefreshToken(authenticate,String.valueOf(user.getId()));
        // 加入redis缓存
        redisUtils.set(
                RedisConstants.REDIS_HEAD + RedisConstants.ACCESS_TOKEN + accessToken,
                user.getId(),
                jwtAccessTokenExpirationDate / 1000
        );
        return ApiResult.success(JwtRspRec.from(accessToken,refreshToken));
    }

    @Override
    public PageResultVo<User> getUserByPage(PageVo page) {
        Pageable pageable = PageUtils.getPageable(page, Sort.Direction.DESC, "updateTime");
        return PageUtils.getPageResult(userRepository.findAll(pageable));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User updateUser(Long id, UserRec userRec) {
        UserRec from = UserRec.from(userRec);
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
    public ApiResult<JwtRspRec> refresh(RefreshRec refreshRec) {
        String bearerToken = refreshRec.refreshToken();
        String accessToken;
        String refreshToken;
        if (!StringUtils.hasText(bearerToken) || !bearerToken.startsWith(BEARER_HEAD)){
            return ApiResult.success(JwtRspRec.from(null, null));
        }
        String token = bearerToken.substring(BEARER_HEAD.length());
        String username = jwtTokenProvider.getUsername(token);
        if (username != null) {
            Login userDetails = userDetailsService.loadUserByUsername(username);
            if (Boolean.TRUE.equals(jwtTokenProvider.validateToken(token,userDetails))){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                accessToken = jwtTokenProvider.generateAccessToken(authToken,String.valueOf(userDetails.getUserId()));
                refreshToken = jwtTokenProvider.generateRefreshToken(authToken,String.valueOf(userDetails.getUserId()));
                // 加入redis缓存
                redisUtils.set(
                        RedisConstants.REDIS_HEAD + RedisConstants.ACCESS_TOKEN + accessToken,
                        userDetails.getUserId(),
                        jwtAccessTokenExpirationDate / 1000
                );
                return ApiResult.success(JwtRspRec.from(accessToken,refreshToken));
            }
        }
        return ApiResult.success(JwtRspRec.from(null, null));
    }

    private User saveUser(Long id, UserRec userRec) {
        Optional<User> userOptional = userRepository.findByPhone(userRec.phone());
        AssertUtils.isTrue(userOptional.isEmpty(), ErrorCode.PHONE_EXIST);
        Optional<Role> roleOptional = roleRepository.findById(userRec.roleId());
        if (roleOptional.isEmpty() || roleOptional.get().getName().equals(ADMIN)){
            throw new BusinessException(ErrorCode.ROLE_NOT_EXIST);
        }
        Optional<Dept> deptOptional = deptRepository.findById(userRec.deptId());
        if (deptOptional.isEmpty()){
            throw new BusinessException(ErrorCode.DEPT_NOT_EXIST);
        }
        User user = new User();
        user.setId(id);
        user.setName(userRec.name());
        user.setUsername(userRec.username());
        // 加密
        user.setPassword(passwordEncoder.encode(userRec.password()));
        user.setGender(userRec.gender());
        user.setEmail(userRec.email());
        user.setPhone(userRec.phone());
        user.setAvatar(userRec.avatar());
        user.setStatus(userRec.status());
        
        User save = userRepository.save(user);
        save.setRole(roleOptional.get());
        save.setDept(deptOptional.get());
        return save;
    }
}
