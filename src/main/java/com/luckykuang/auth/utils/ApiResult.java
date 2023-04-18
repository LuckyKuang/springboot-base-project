package com.luckykuang.auth.utils;

import com.luckykuang.auth.constant.ErrorCodeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 统一返回类
 * @author luckykuang
 * @date 2023/4/10 19:47
 */
@Getter
@Setter
@AllArgsConstructor
public class ApiResult<T> {

    /**
     * 错误码
     */
    @Schema(title = "错误码")
    private Integer code;
    /**
     * 错误提示，用户可阅读
     */
    @Schema(title = "错误提示")
    private String message;
    /**
     * 返回数据
     */
    @Schema(title = "返回数据")
    private T data;

    public static <T> ApiResult<T> success(){
        return commonResult(ErrorCodeEnum.SUCCESS.getCode(), ErrorCodeEnum.SUCCESS.getMessage(), null);
    }
    public static <T> ApiResult<T> success(T data){
        return commonResult(ErrorCodeEnum.SUCCESS.getCode(), ErrorCodeEnum.SUCCESS.getMessage(), data);
    }
    public static <T> ApiResult<T> failed(ErrorCodeEnum errorCodeEnum){
        return commonResult(errorCodeEnum.getCode(),errorCodeEnum.getMessage(),null);
    }
    public static <T> ApiResult<T> failed(ErrorCodeEnum errorCodeEnum,T data){
        return commonResult(errorCodeEnum.getCode(),errorCodeEnum.getMessage(),data);
    }
    public static <T> ApiResult<T> failed(Integer code, String message){
        return commonResult(code,message,null);
    }
    private static <T> ApiResult<T> commonResult(Integer code, String message, T data){
        return new ApiResult<>(code,message,data);
    }
}
