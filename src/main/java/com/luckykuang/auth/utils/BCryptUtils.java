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

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Security 解密工具类
 * @author luckykuang
 * @date 2023/5/26 17:26
 */
public class BCryptUtils {

    private BCryptUtils(){}

    private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    /**
     * 加密
     * @param password 明文
     * @return 密文
     */
    public static String encode(String password){
        return PASSWORD_ENCODER.encode(password);
    }

    /**
     * 校验密码
     * @param password 明文
     * @param encodePassword 密文
     * @return 正确-true 错误-false
     */
    public static boolean match(String password,String encodePassword){
        return PASSWORD_ENCODER.matches(password,encodePassword);
    }
}
