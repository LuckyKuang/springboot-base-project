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
import com.luckykuang.auth.model.Login;
import com.luckykuang.auth.model.Role;
import com.luckykuang.auth.model.User;
import com.luckykuang.auth.record.JwtRspRec;
import com.luckykuang.auth.record.SignInRec;
import com.luckykuang.auth.record.SignOnRec;
import com.luckykuang.auth.repository.RoleRepository;
import com.luckykuang.auth.repository.UserRepository;
import com.luckykuang.auth.service.UserService;
import com.luckykuang.auth.utils.AssertUtils;
import com.luckykuang.auth.utils.PageUtils;
import com.luckykuang.auth.utils.RedisUtils;
import com.luckykuang.auth.vo.PageResultVo;
import com.luckykuang.auth.vo.PageVo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

import java.io.IOException;
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
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Value("${app.jwt-expiration.access-token-milliseconds:10000}")
    private long jwtAccessTokenExpirationDate;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final RedisUtils redisUtils;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    public User addUser(SignOnRec sign) {
        SignOnRec from = SignOnRec.from(sign);
        Optional<User> usersOptional = userRepository.findByUsername(from.username());
        AssertUtils.isTrue(usersOptional.isEmpty(), ErrorCode.NAME_EXIST);
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
    public JwtRspRec signIn(SignInRec sign) {
        SignInRec from = SignInRec.from(sign);
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
        return JwtRspRec.from(accessToken,refreshToken);
    }

    @Override
    public ApiResult<Void> signOn(SignOnRec sign) {
        SignOnRec from = SignOnRec.from(sign);
        saveUser(null,from);
        return ApiResult.success();
    }

    @Override
    public PageResultVo<User> getUserByPage(PageVo page) {
        Pageable pageable = PageUtils.getPageable(page, Sort.Direction.DESC, "updateTime");
        return PageUtils.getPageResult(userRepository.findAll(pageable));
    }

    @Override
    public User updateUser(Long id, SignOnRec sign) {
        SignOnRec from = SignOnRec.from(sign);
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
    public JwtRspRec refresh(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String bearerToken = request.getHeader(AUTHORIZATION);
        String accessToken;
        String refreshToken;
        if (!StringUtils.hasText(bearerToken) || !bearerToken.startsWith(BEARER_HEAD)){
            return JwtRspRec.from(null, null);
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
                return JwtRspRec.from(accessToken,refreshToken);
            }
        }
        return JwtRspRec.from(null, null);
    }

    private User saveUser(Long id, SignOnRec sign) {
        Optional<Role> roleOptional = roleRepository.findById(sign.roleId());
        if (roleOptional.isEmpty() || roleOptional.get().getRoleName().equals(ADMIN)){
            throw new BusinessException(ErrorCode.ROLE_NOT_EXIST);
        }
        User user = new User();
        user.setId(id);
        user.setName(sign.name());
        user.setUsername(sign.username());
        // 加密
        user.setPassword(passwordEncoder.encode(sign.password()));
        user.setEmail(sign.email());
        user.setPhone(sign.phone());
        user.setUserStatus(sign.userStatus());
        if (id == null && user.getCreateBy() == null){
            user.setCreateBy(0L);
        }
        if (id == null && user.getUpdateBy() == null){
            user.setUpdateBy(0L);
        }
        User save = userRepository.save(user);
        save.setRole(roleOptional.get());
        return save;
    }
}
