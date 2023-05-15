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

package com.luckykuang.auth.config;

import com.luckykuang.auth.base.RequestContext;
import com.luckykuang.auth.config.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import static com.luckykuang.auth.constants.CoreConstants.BEARER_HEAD;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * @author luckykuang
 * @date 2023/4/20 17:39
 */
@Component
@RequiredArgsConstructor
public class WebMvcHandlerInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String bearerToken = request.getHeader(AUTHORIZATION);
        if (!StringUtils.hasText(bearerToken) || !bearerToken.startsWith(BEARER_HEAD)){
            return false;
        }
        String token = bearerToken.substring(BEARER_HEAD.length());
        String userId = jwtTokenProvider.getUserId(token);
        RequestContext.setUserId(Long.valueOf(userId));
        RequestContext.setToken(token);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 销毁本地缓存
        RequestContext.clear();
    }
}
