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

package com.luckykuang.auth.constants;

/**
 * 常量类
 * @author luckykuang
 * @date 2023/4/24 17:25
 */
public interface CoreConstants {


    int NO = 0;
    int YES = 1;

    int TING_INT_TRUE = 1;
    int TING_INT_FALSE = 0;

    int DEFAULT = 0;

    /**
     * 符号
     */
    String FORWARD_SLASH = "/";
    String DOT = ".";
    String RISK = ":";
    String COMMA = ",";
    String UNDERLINE = "_";
    String AND = "&";

    String BEARER_HEAD = "Bearer ";
    String BEARER = "Bearer";
    String AUTHORITIES = "authorities";

    String ADMIN = "ADMIN";

    String ROLE = "ROLE";

    String ROLE_UNDERLINE = ROLE + UNDERLINE;

}
