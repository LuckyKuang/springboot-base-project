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

package com.luckykuang.auth.base;

import java.util.Optional;

/**
 * 请求信息捕获 全局缓存
 * @author luckykuang
 * @date 2023/4/10 19:31
 */
public class RequestContext {

    private RequestContext(){}

    private static final ThreadLocal<Long> currentUserId = new ThreadLocal<>();
    private static final ThreadLocal<String> currentTokenId = new ThreadLocal<>();


    /**
     * 设置 userId
     */
    public static void setUserId(Long userId) {
        currentUserId.set(userId);
    }

    /**
     * 获取 userId
     */
    public static Optional<Long> getUserId() {
        return Optional.ofNullable(currentUserId.get());
    }

    /**
     * 设置 token
     */
    public static void setToken(String token) {
        currentTokenId.set(token);
    }

    /**
     * 获取 token
     */
    public static Optional<String> getToken() {
        return Optional.ofNullable(currentTokenId.get());
    }

    /**
     * 清除ThreadLocal缓存
     */
    public static void clear() {
        currentUserId.remove();
        currentTokenId.remove();
    }
}
