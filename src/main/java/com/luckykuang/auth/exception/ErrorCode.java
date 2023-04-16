package com.luckykuang.auth.exception;

import com.luckykuang.auth.constant.ErrorCodeEnum;

/**
 * 错误码纪录类
 * 全局错误码枚举 参见 {@link ErrorCodeEnum}
 *
 * @param code 错误码
 * @param message  错误提示
 * @author luckykuang
 * @date 2023/4/10 19:34
 */
public record ErrorCode(Integer code, String message) {
}
