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
import com.luckykuang.auth.utils.ApplicationContextUtils;
import com.luckykuang.auth.utils.RedisUtils;
import com.luckykuang.auth.utils.ResponseUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author fankuangyong
 * @date 2023/5/17 17:09
 */
public class CaptchaFilter extends OncePerRequestFilter {

    public static final String CAPTCHA_KEY = "captchaKey";
    public static final String CAPTCHA_CACHE_KEY = "captchaCacheKey";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String servletPath = request.getServletPath();
        // 检验登录接口的验证码
        if (servletPath.equals("/auth/v1/sign/login")) {
            // 请求中的验证码
            String captchaKey = request.getParameter(CAPTCHA_KEY);
            // 缓存中的验证码
            String captchaCacheKey = request.getParameter(CAPTCHA_CACHE_KEY);
            RedisUtils redisUtils = ApplicationContextUtils.getBean(RedisUtils.class);
            Object cacheCaptchaKey = redisUtils.get(RedisConstants.REDIS_HEAD + RedisConstants.CAPTCHA_KEY + captchaCacheKey);
            if (cacheCaptchaKey == null) {
                ResponseUtils.writeErrMsg(response, ErrorCode.CAPTCHA_TIMEOUT);
            } else {
                // 验证码比对
                if (cacheCaptchaKey.equals(captchaKey)) {
                    filterChain.doFilter(request, response);
                } else {
                    ResponseUtils.writeErrMsg(response, ErrorCode.CAPTCHA_ERROR);
                }
            }
        } else {
            // 非登录接口放行
            filterChain.doFilter(request, response);
        }
    }
}
