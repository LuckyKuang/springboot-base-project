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
import com.luckykuang.auth.exception.BusinessException;
import com.luckykuang.auth.service.impl.UserDetailsServiceImpl;
import com.luckykuang.auth.utils.RedisUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

import static com.luckykuang.auth.constants.CoreConstants.BEARER_HEAD;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

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
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final RedisUtils redisUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String servletPath = request.getServletPath();
            if (servletPath.startsWith("/auth/v1/sign") || servletPath.equals("/favicon.ico")
                    || servletPath.startsWith("/doc.html") || servletPath.startsWith("/swagger-ui")
                    || servletPath.startsWith("/webjars") || servletPath.startsWith("/v3/api-docs")) {
                filterChain.doFilter(request, response);
                return;
            }
            String bearerToken = request.getHeader(AUTHORIZATION);
            if (!StringUtils.hasText(bearerToken) || !bearerToken.startsWith(BEARER_HEAD)) {
                throw new BusinessException("令牌为空");
            }
            String token = bearerToken.substring(BEARER_HEAD.length());
            // 读取redis缓存
            Object tokenCache = redisUtils.get(RedisConstants.REDIS_HEAD + RedisConstants.ACCESS_TOKEN + token);
            if (tokenCache == null) {
                throw new BusinessException("令牌已过期：" + token);
            }
            String username = jwtTokenProvider.getUsername(token);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (Boolean.TRUE.equals(jwtTokenProvider.validateToken(token, userDetails))) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            // 让@RestControllerAdvice类的Exception方法来处理security全局异常
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }
}
