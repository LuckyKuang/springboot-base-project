package com.luckykuang.auth.utils;

import com.luckykuang.auth.constant.ErrorCodeEnum;
import com.luckykuang.auth.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * 断言类
 * @author luckykuang
 * @date 2023/4/10 20:49
 */
public class AssertUtils {

    private AssertUtils(){}

    /**
     * 断言这个 boolean 为 true
     * <p>为 false 则抛出异常</p>
     *
     * @param expression    boolean 值
     * @param errorCodeEnum     错误消息
     */
    public static void isTrue(boolean expression, ErrorCodeEnum errorCodeEnum) {
        if (!expression) {
            throw new BusinessException(errorCodeEnum);
        }
    }

    /**
     * 断言这个 boolean 为 false
     * <p>为 true 则抛出异常</p>
     *
     * @param expression    boolean 值
     * @param errorCodeEnum     错误消息
     */
    public static void isFalse(boolean expression, ErrorCodeEnum errorCodeEnum) {
        isTrue(!expression, errorCodeEnum);
    }

    /**
     * 断言这个 object 为 null
     * <p>不为 null 则抛异常</p>
     *
     * @param object     对象
     * @param errorCodeEnum  错误消息
     */
    public static void isNull(Object object, ErrorCodeEnum errorCodeEnum) {
        isTrue(object == null, errorCodeEnum);
    }

    /**
     * 断言这个 object 不为 null
     * <p>为 null 则抛异常</p>
     *
     * @param object     对象
     * @param errorCodeEnum  错误消息
     */
    public static void notNull(Object object, ErrorCodeEnum errorCodeEnum) {
        isTrue(object != null, errorCodeEnum);
    }

    /**
     * 断言这个 value 不为 empty
     * <p>为 empty 则抛异常</p>
     *
     * @param value      字符串
     * @param errorCodeEnum  错误消息
     */
    public static void notEmpty(String value, ErrorCodeEnum errorCodeEnum) {
        isTrue(StringUtils.isNotBlank(value), errorCodeEnum);
    }

    /**
     * 断言这个 map 不为 empty
     * <p>为 empty 则抛异常</p>
     *
     * @param map        集合
     * @param errorCodeEnum  错误消息
     */
    public static void notEmpty(Map<?, ?> map, ErrorCodeEnum errorCodeEnum) {
        isTrue(map != null && !map.isEmpty(), errorCodeEnum);
    }

    /**
     * 断言这个 map 为 empty
     * <p>为 empty 则抛异常</p>
     *
     * @param map        集合
     * @param errorCodeEnum  错误消息
     */
    public static void isEmpty(Map<?, ?> map, ErrorCodeEnum errorCodeEnum) {
        isTrue(map == null || map.isEmpty(), errorCodeEnum);
    }

    /**
     * 断言这个 数组 不为 empty
     * <p>为 empty 则抛异常</p>
     *
     * @param array      数组
     * @param errorCodeEnum  错误消息
     */
    public static void notEmpty(Object[] array, ErrorCodeEnum errorCodeEnum) {
        isTrue(array != null && array.length > 0, errorCodeEnum);
    }
}
