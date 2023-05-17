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

import com.luckykuang.auth.constants.enums.ErrorCode;
import com.luckykuang.auth.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * 断言工具类
 * @author luckykuang
 * @date 2023/4/21 15:10
 */
public final class AssertUtils {

    private AssertUtils(){}

    /**
     * 断言这个 boolean 为 true
     * <p>为 false 则抛出异常</p>
     *
     * @param expression    boolean 值
     * @param errorCode     错误消息
     */
    public static void isTrue(boolean expression, ErrorCode errorCode) {
        if (!expression) {
            throw new BusinessException(errorCode);
        }
    }

    /**
     * 断言这个 boolean 为 false
     * <p>为 true 则抛出异常</p>
     *
     * @param expression    boolean 值
     * @param errorCode     错误消息
     */
    public static void isFalse(boolean expression, ErrorCode errorCode) {
        isTrue(!expression, errorCode);
    }

    /**
     * 断言这个 object 为 null
     * <p>不为 null 则抛异常</p>
     *
     * @param object     对象
     * @param errorCode  错误消息
     */
    public static void isNull(Object object, ErrorCode errorCode) {
        isTrue(object == null, errorCode);
    }

    /**
     * 断言这个 object 不为 null
     * <p>为 null 则抛异常</p>
     *
     * @param object     对象
     * @param errorCode  错误消息
     */
    public static void notNull(Object object, ErrorCode errorCode) {
        isTrue(object != null, errorCode);
    }

    /**
     * 断言这个 value 不为 empty
     * <p>为 empty 则抛异常</p>
     *
     * @param value      字符串
     * @param errorCode  错误消息
     */
    public static void notEmpty(String value, ErrorCode errorCode) {
        isTrue(StringUtils.isNotBlank(value), errorCode);
    }

    /**
     * 断言这个 map 不为 empty
     * <p>为 empty 则抛异常</p>
     *
     * @param map        集合
     * @param errorCode  错误消息
     */
    public static void notEmpty(Map<?, ?> map, ErrorCode errorCode) {
        isTrue(map != null && !map.isEmpty(), errorCode);
    }

    /**
     * 断言这个 map 为 empty
     * <p>为 empty 则抛异常</p>
     *
     * @param map        集合
     * @param errorCode  错误消息
     */
    public static void isEmpty(Map<?, ?> map, ErrorCode errorCode) {
        isTrue(map == null || map.isEmpty(), errorCode);
    }

    /**
     * 断言这个 数组 不为 empty
     * <p>为 empty 则抛异常</p>
     *
     * @param array      数组
     * @param errorCode  错误消息
     */
    public static void notEmpty(Object[] array, ErrorCode errorCode) {
        isTrue(array != null && array.length > 0, errorCode);
    }
}
