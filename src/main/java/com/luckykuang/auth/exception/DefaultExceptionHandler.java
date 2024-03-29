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

package com.luckykuang.auth.exception;

import com.luckykuang.auth.base.ApiResult;
import com.luckykuang.auth.constants.enums.ErrorCode;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * 全局异常处理
 * @author luckykuang
 * @date 2023/4/21 15:11
 */
@Slf4j
@RestControllerAdvice
public class DefaultExceptionHandler {
    /**
     * 处理 @Valid 校验不通过产生的异常
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ApiResult<?> argumentNotValidHandler(MethodArgumentNotValidException ex) {
        log.warn("[methodArgumentNotValidException]", ex);
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(s -> s.getField() + s.getDefaultMessage()).collect(Collectors.joining(";"));
        return ApiResult.failed(ErrorCode.BAD_REQUEST, message);
    }

    /**
     * 处理 @Validated 校验不通过产生的异常
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ApiResult<?> constraintViolationExceptionHandler(ConstraintViolationException ex) {
        log.warn("[constraintViolationExceptionHandler]", ex);
        String message = ex.getConstraintViolations().stream()
                .map(val -> val.getPropertyPath() + val.getMessage()).collect(Collectors.joining(";"));
        return ApiResult.failed(ErrorCode.BAD_REQUEST, message);
    }

    /**
     * 处理 SpringMVC 请求方法不正确
     *
     * 例如说，A 接口的方法为 GET 方式，结果请求方法为 POST 方式，导致不匹配
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ApiResult<?> httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException ex) {
        log.warn("[httpRequestMethodNotSupportedExceptionHandler]", ex);
        return ApiResult.failed(ErrorCode.METHOD_NOT_ALLOWED_REQUEST, ex.getMessage());
    }

    /**
     * 处理 SpringMVC 请求体不正确
     *
     * 例如说，接口的请求方法为 POST 方式，需要form-data参数，传过来的确实body参数，导致不匹配
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ApiResult<?> httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException ex) {
        log.warn("[httpMessageNotReadableExceptionHandler]", ex);
        return ApiResult.failed(ErrorCode.BODY_NOT_ALLOWED_REQUEST, ex.getMessage());
    }

    /**
     * 处理 Spring Security 用户名不存在
     * @param ex
     * @return
     */
    @ExceptionHandler(value = InternalAuthenticationServiceException.class)
    public ApiResult<?> internalAuthenticationServiceExceptionHandler(InternalAuthenticationServiceException ex) {
        log.info("[internalAuthenticationServiceExceptionHandler]:{}", ex.getMessage());
        return ApiResult.failed(ErrorCode.USERNAME_NOT_EXIST,ex.getMessage());
    }

    /**
     * 处理 Spring Security 用户名或密码错误
     * @param ex
     * @return
     */
    @ExceptionHandler(value = BadCredentialsException.class)
    public ApiResult<?> badCredentialsExceptionHandler(BadCredentialsException ex) {
        log.info("[badCredentialsExceptionHandler]:{}", ex.getMessage());
        return ApiResult.failed(ErrorCode.USERNAME_AND_PASSWORD_IS_ERROR,ex.getMessage());
    }

    /**
     * 处理 Spring Security 无权限访问
     * @param ex
     * @return
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    public ApiResult<?> accessDeniedExceptionHandler(AccessDeniedException ex) {
        log.info("[accessDeniedExceptionHandler]:{}", ex.getMessage());
        return ApiResult.failed(ErrorCode.FORBIDDEN,ex.getMessage());
    }

    /**
     * 处理业务异常 参见 {@link ErrorCode}
     *
     */
    @ExceptionHandler(value = BusinessException.class)
    public ApiResult<?> businessExceptionHandler(BusinessException ex) {
        log.info("[businessExceptionHandler]:{}", ex.getMessage());
        return ApiResult.failed(ex.getCode(), ex.getMessage());
    }

    /**
     * 兜底所有异常处理
     */
    @ExceptionHandler(value = Exception.class)
    public ApiResult<?> defaultExceptionHandler(Exception ex) {
        log.error("[defaultExceptionHandler]", ex);
        return ApiResult.failed(ErrorCode.UNKNOWN,ex.getMessage());
    }
}
