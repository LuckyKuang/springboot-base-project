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

package com.luckykuang.auth.constants.enums;

/**
 * 错误码枚举类
 * @author luckykuang
 * @date 2023/4/10 20:02
 */
public enum ErrorCode {
    SUCCESS(200,"操作成功"),
    UNKNOWN(9999,"操作失败"),
    BAD_REQUEST(400,"请求参数不正确"),
    TOKEN_INVALID(401, "Token已过期"),
    FORBIDDEN(403, "无权限访问"),
    NOT_FOUND_REQUEST(404, "请求未找到"),
    METHOD_NOT_ALLOWED_REQUEST(405, "请求方法不正确"),
    BODY_NOT_ALLOWED_REQUEST(406, "请求体不正确"),
    TOKEN_IS_EMPTY(407, "Token为空"),
    TOKEN_ILLEGAL(408, "Token非法"),
    TOKEN_VERIFICATION_FAILED(409, "Token验证失败"),
    TOKEN_INCORRECT(410, "Token不正确"),
    TOKEN_CREATE_EXCEPTION(410, "Token创建异常"),
    FAILED_REQUEST(423, "请求失败，请稍后重试"),
    TOO_MANY_REQUESTS(429, "请求过于频繁，请稍后重试"),
    INTERNAL_SERVER_ERROR(500, "系统异常，请联系管理员"),
    NOT_IMPLEMENTED(501, "功能未实现/未开启"),
    REPEATED_REQUESTS(900, "重复请求，请稍后重试"),
    DEMO_DENY(901, "演示模式，禁止写操作"),
    NAME_EXIST(1000,"名称已存在"),
    ID_NOT_EXIST(1001,"ID不存在"),
    PHONE_EXIST(1002,"手机号码已存在"),
    EMAIL_EXIST(1003,"邮箱已存在"),
    USERNAME_EXIST(1004,"用户名已存在"),
    USERNAME_NOT_EXIST(1005,"用户不存在"),
    FIELD_EXIST(1006,"字段已存在"),
    ACCOUNT_DISABLE(1007,"账户被禁用"),
    ACCOUNT_LOCKED(1008,"账户被锁定"),
    USERNAME_AND_PASSWORD_IS_ERROR(1009,"用户名或密码错误"),
    IMAGE_TO_BASE64_ERROR(1010,"图片转Base64异常"),
    FILE_CREATED_ERROR(1011,"文件创建异常"),
    BASE64_TO_IMAGE_ERROR(1012,"Base64转图片异常"),
    USER_NOT_EXIST(1013,"用户不存在"),
    ROLE_NOT_EXIST(1014,"角色不存在"),
    PERMISSION_NOT_EXIST(1015,"权限不存在"),
    DEPT_NOT_EXIST(1016,"部门不存在"),
    CAPTCHA_TIMEOUT(1017, "验证码已过期"),
    CAPTCHA_ERROR(1018, "验证码错误"),
    CAPTCHA_CONFIG_ERROR(1019, "验证码配置信息有无,请联系管理员!"),
    ;
    private final Integer code;
    private final String message;

    ErrorCode(Integer code, String message) {
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
