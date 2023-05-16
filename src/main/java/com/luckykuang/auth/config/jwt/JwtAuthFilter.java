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

package com.luckykuang.auth.config.jwt;

import com.luckykuang.auth.constants.RedisConstants;
import com.luckykuang.auth.enums.ErrorCode;
import com.luckykuang.auth.service.impl.UserDetailsServiceImpl;
import com.luckykuang.auth.utils.RedisUtils;
import com.luckykuang.auth.utils.ResponseUtils;
import com.luckykuang.auth.utils.TokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author luckykuang
 * @date 2023/4/22 17:55
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsServiceImpl userDetailsService;
    private final RedisUtils redisUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String servletPath = request.getServletPath();
        if (servletPath.equals("/auth/v1/sign/login") || servletPath.equals("/auth/v1/sign/refresh")
                || servletPath.startsWith("/v3/api-docs") || servletPath.startsWith("/swagger-ui")
                || servletPath.startsWith("/swagger-resources") || servletPath.startsWith("/webjars")
                || servletPath.startsWith("/doc.html") || servletPath.startsWith("/favicon.ico")) {
            filterChain.doFilter(request, response);
        }
        else {
            String token = TokenUtils.resolveToken(request);
            if (StringUtils.isNotBlank(token) && SecurityContextHolder.getContext().getAuthentication() == null) {
                // 读取redis缓存
                Object tokenCache = redisUtils.get(RedisConstants.REDIS_HEAD + RedisConstants.ACCESS_TOKEN + token);
                if (tokenCache == null) {
                    ResponseUtils.writeErrMsg(response, ErrorCode.TOKEN_INVALID);
                }
                String username = jwtTokenProvider.getUsername(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (Boolean.TRUE.equals(jwtTokenProvider.validateToken(token, userDetails))) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    filterChain.doFilter(request, response);
                } else {
                    ResponseUtils.writeErrMsg(response, ErrorCode.TOKEN_INVALID);
                }
            }else {
                ResponseUtils.writeErrMsg(response, ErrorCode.TOKEN_INVALID);
            }
        }
    }
}
