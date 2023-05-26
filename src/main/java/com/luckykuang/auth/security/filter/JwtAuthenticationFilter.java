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

package com.luckykuang.auth.security.filter;

import com.luckykuang.auth.constants.RedisConstants;
import com.luckykuang.auth.constants.enums.ErrorCode;
import com.luckykuang.auth.security.utils.JwtTokenProvider;
import com.luckykuang.auth.utils.RedisUtils;
import com.luckykuang.auth.utils.RequestUtils;
import com.luckykuang.auth.utils.ResponseUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

import static com.luckykuang.auth.constants.CoreConstants.CAPTCHA_ANSWER;
import static com.luckykuang.auth.constants.CoreConstants.CAPTCHA_KEY;

/**
 * @author luckykuang
 * @date 2023/4/22 17:55
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisUtils redisUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String servletPath = request.getServletPath();
        // 登录接口校验
        if (servletPath.equals("/auth/v1/sign/login")) {
            // 用户输入的验证码
            String captchaAnswer = request.getHeader(CAPTCHA_ANSWER);
            // 缓存中的验证码key
            String captchaKey = request.getHeader(CAPTCHA_KEY);
            // 查缓存中的验证码
            Object cacheCaptchaKey = redisUtils.get(RedisConstants.REDIS_HEAD + RedisConstants.CAPTCHA_CACHE_KEY + captchaKey);
            if (cacheCaptchaKey == null) {
                ResponseUtils.writeErrMsg(response, ErrorCode.CAPTCHA_TIMEOUT);
            } else {
                // 验证码比对
                if (cacheCaptchaKey.equals(captchaAnswer)) {
                    filterChain.doFilter(request, response);
                } else {
                    ResponseUtils.writeErrMsg(response, ErrorCode.CAPTCHA_ERROR);
                }
            }
        }
        // 其他接口校验
        else {
            Optional<String> token = RequestUtils.resolveToken(request);
            if (token.isPresent() && SecurityContextHolder.getContext().getAuthentication() == null) {
                // 读取redis缓存
                Object tokenCache = redisUtils.get(RedisConstants.REDIS_HEAD + RedisConstants.ACCESS_TOKEN + token.get());
                if (tokenCache == null) {
                    ResponseUtils.writeErrMsg(response, ErrorCode.TOKEN_INVALID);
                }
                Authentication authentication = jwtTokenProvider.getAuthentication(token.get());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        }
    }
}
