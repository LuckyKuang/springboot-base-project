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
import com.luckykuang.auth.constants.RedisConstants;
import com.luckykuang.auth.constants.enums.ErrorCode;
import com.luckykuang.auth.exception.BusinessException;
import com.luckykuang.auth.model.Dept;
import com.luckykuang.auth.model.Role;
import com.luckykuang.auth.model.User;
import com.luckykuang.auth.repository.DeptRepository;
import com.luckykuang.auth.repository.RoleRepository;
import com.luckykuang.auth.repository.UserRepository;
import com.luckykuang.auth.request.LoginReq;
import com.luckykuang.auth.request.RefreshReq;
import com.luckykuang.auth.request.UserReq;
import com.luckykuang.auth.response.CaptchaRsp;
import com.luckykuang.auth.response.TokenRsp;
import com.luckykuang.auth.security.userdetails.LoginUserDetails;
import com.luckykuang.auth.security.userdetails.LoginUserDetailsService;
import com.luckykuang.auth.security.utils.JwtTokenProvider;
import com.luckykuang.auth.service.EasyCaptchaService;
import com.luckykuang.auth.service.UserService;
import com.luckykuang.auth.utils.AssertUtils;
import com.luckykuang.auth.utils.PageUtils;
import com.luckykuang.auth.utils.RedisUtils;
import com.luckykuang.auth.vo.PageResultVo;
import com.luckykuang.auth.vo.PageVo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static com.luckykuang.auth.constants.CoreConstants.ADMIN;
import static com.luckykuang.auth.constants.CoreConstants.BEARER_HEAD;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

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
    private final LoginUserDetailsService userDetailsService;
    private final EasyCaptchaService easyCaptchaService;

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
    public ApiResult<TokenRsp> login(LoginReq loginReq) {
        LoginReq from = LoginReq.from(loginReq);
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
        return ApiResult.success(TokenRsp.from(accessToken,refreshToken));
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
    public ApiResult<TokenRsp> refresh(RefreshReq refreshReq) {
        String bearerToken = refreshReq.refreshToken();
        String accessToken;
        String refreshToken;
        if (!StringUtils.hasText(bearerToken) || !bearerToken.startsWith(BEARER_HEAD)){
            return ApiResult.success(TokenRsp.from(null, null));
        }
        String token = bearerToken.substring(BEARER_HEAD.length());
        String username = jwtTokenProvider.getUsername(token);
        if (username != null) {
            LoginUserDetails userDetails = userDetailsService.loadUserByUsername(username);
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
                return ApiResult.success(TokenRsp.from(accessToken,refreshToken));
            }
        }
        return ApiResult.success(TokenRsp.from(null, null));
    }

    @Override
    public CaptchaRsp getCaptcha() {
        return easyCaptchaService.getCaptcha();
    }

    @Override
    public ApiResult<Void> logout(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION);
        if (!StringUtils.hasText(bearerToken) || !bearerToken.startsWith(BEARER_HEAD)){
            return ApiResult.failed(ErrorCode.BAD_REQUEST);
        }
        String token = bearerToken.substring(BEARER_HEAD.length());
        // 删除redis缓存
        redisUtils.del(RedisConstants.REDIS_HEAD + RedisConstants.ACCESS_TOKEN + token);
        // 清除security缓存
        SecurityContextHolder.clearContext();
        return ApiResult.success();
    }

    private User saveUser(Long id, UserReq userReq) {
        Optional<User> userOptional = userRepository.findByPhone(userReq.phone());
        AssertUtils.isTrue(userOptional.isEmpty(), ErrorCode.PHONE_EXIST);
        Optional<Role> roleOptional = roleRepository.findById(userReq.roleId());
        if (roleOptional.isEmpty() || roleOptional.get().getName().equals(ADMIN)){
            throw new BusinessException(ErrorCode.ROLE_NOT_EXIST);
        }
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
        save.setRole(roleOptional.get());
        save.setDept(deptOptional.get());
        return save;
    }
}
