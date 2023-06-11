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

package com.luckykuang.auth.security.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * jwt配置
 * @author luckykuang
 * @date 2023/5/17 14:09
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class TokenSecretProperties {
    // 加密盐
    private String secret;
    // access_token过期时间(ms)
    private long accessTokenExpirationDate;
    // refresh_token过期时间(ms)
    private long refreshTokenExpirationDate;
}
