package com.luckykuang.auth.constant;

/**
 * 错误码枚举类
 * @author luckykuang
 * @date 2023/4/10 20:02
 */
public enum ErrorCodeEnum {
    SUCCESS(200,"操作成功"),
    UNKNOWN(9999,"操作失败"),
    BAD_REQUEST(400,"请求参数不正确"),
    UNAUTHORIZED(401, "账号未登录"),
    FORBIDDEN(403, "没有该操作权限"),
    NOT_FOUND_REQUEST(404, "请求未找到"),
    METHOD_NOT_ALLOWED_REQUEST(405, "请求方法不正确"),
    FAILED_REQUEST(423, "请求失败，请稍后重试"),
    TOO_MANY_REQUESTS(429, "请求过于频繁，请稍后重试"),
    INTERNAL_SERVER_ERROR(500, "系统异常"),
    NOT_IMPLEMENTED(501, "功能未实现/未开启"),
    REPEATED_REQUESTS(900, "重复请求，请稍后重试"),
    DEMO_DENY(901, "演示模式，禁止写操作"),
    NAME_EXIST(1000,"名称已存在"),
    ID_NOT_EXIST(1001,"ID不存在"),
    PHONE_EXIST(1002,"手机号码已存在"),
    EMAIL_EXIST(1003,"邮箱已存在"),
    USERNAME_EXIST(1003,"用户名已存在"),
    FIELD_EXIST(1003,"字段已存在"),
    ;
    private final Integer code;
    private final String message;

    ErrorCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
