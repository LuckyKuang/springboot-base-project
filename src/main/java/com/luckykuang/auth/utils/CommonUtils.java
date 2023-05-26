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

import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;

/**
 * @author luckykuang
 * @date 2023/5/17 15:46
 */
public class CommonUtils {

    private CommonUtils(){}

    /**
     * 获取去除"-"后的uuid
     * @param flag true-获取去除"-"后的uuid  false-获取原始uuid
     * @return
     */
    public static String getUUID(boolean flag){
        if (flag){
            return UUID.randomUUID().toString().replace("-","");
        }
        return UUID.randomUUID().toString();
    }

    /**
     * List转String 带引号
     * @param delimiter 用于分割的字符
     * @param elements 需要拆分的数组
     * @return 分割后的字符
     */
    public static String join(CharSequence delimiter,
                              Iterable<? extends CharSequence> elements) {
        Objects.requireNonNull(delimiter);
        Objects.requireNonNull(elements);
        StringJoiner joiner = new StringJoiner(delimiter);
        for (CharSequence cs: elements) {
            joiner.add("\"" + cs + "\"");
        }
        return joiner.toString();
    }
}
