package com.luckykuang.auth.exception;

import com.luckykuang.auth.constant.ErrorCodeEnum;
import lombok.Getter;

import java.io.Serial;

/**
 * 自定义错误处理
 * @author luckykuang
 * @date 2023/4/10 19:34
 */
@Getter
public class BusinessException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 5852935658136753856L;
    /**
     * 错误码
     */
    private final Integer code;
    /**
     * 错误提示
     */
    private final String message;

    public BusinessException(ErrorCodeEnum errorCodeEnum){
        super();
        this.code = errorCodeEnum.getCode();
        this.message = errorCodeEnum.getMessage();
    }
}
