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

import com.luckykuang.auth.constants.RedisConstants;
import com.luckykuang.auth.utils.RedisUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import static com.luckykuang.auth.constants.CoreConstants.BEARER_HEAD;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * @author luckykuang
 * @date 2023/5/4 15:54
 */
@Service
@RequiredArgsConstructor
public class LogoutServiceImpl implements LogoutHandler {

    private final RedisUtils redisUtils;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String bearerToken = request.getHeader(AUTHORIZATION);
        if (!StringUtils.hasText(bearerToken) || !bearerToken.startsWith(BEARER_HEAD)){
            return;
        }
        String token = bearerToken.substring(BEARER_HEAD.length());
        // 删除redis缓存
        redisUtils.del(RedisConstants.REDIS_HEAD + RedisConstants.ACCESS_TOKEN + token);
        // 清除security缓存
        SecurityContextHolder.clearContext();
    }
}
