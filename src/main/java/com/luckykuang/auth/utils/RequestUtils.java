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

package com.luckykuang.auth.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;

import static com.luckykuang.auth.constants.CoreConstants.BEARER_HEAD;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * 请求处理工具类
 * @author fankuangyong
 * @date 2023/5/15 14:32
 */
public class RequestUtils {
    private RequestUtils(){}

    /**
     * 将请求头的token截取返回
     */
    public static String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_HEAD)) {
            return bearerToken.substring(BEARER_HEAD.length());
        }
        return null;
    }
}