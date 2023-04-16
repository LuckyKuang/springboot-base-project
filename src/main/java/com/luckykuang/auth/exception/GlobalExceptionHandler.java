package com.luckykuang.auth.exception;

import com.luckykuang.auth.constant.ErrorCodeEnum;
import com.luckykuang.auth.utils.ApiResult;
import com.luckykuang.auth.config.RequestContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.nio.file.AccessDeniedException;
import java.util.stream.Collectors;

/**
 * 全局异常处理
 * @author luckykuang
 * @date 2023/4/11 10:44
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 处理 SpringMVC 请求参数缺失
     *
     * 例如说，接口上设置了 @RequestParam("xx") 参数，结果并未传递 xx 参数
     */
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ApiResult<?> missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException ex) {
        log.warn("[missingServletRequestParameterExceptionHandler]", ex);
        return ApiResult.failed(ErrorCodeEnum.BAD_REQUEST, String.format("请求参数缺失:%s", ex.getParameterName()));
    }

    /**
     * 处理 SpringMVC 请求参数类型错误
     *
     * 例如说，接口上设置了 @RequestParam("xx") 参数为 Integer，结果传递 xx 参数类型为 String
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ApiResult<?> methodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException ex) {
        log.warn("[missingServletRequestParameterExceptionHandler]", ex);
        return ApiResult.failed(ErrorCodeEnum.BAD_REQUEST, String.format("请求参数类型错误:%s", ex.getMessage()));
    }

    /**
     * 处理 SpringMVC 参数校验不正确
     */
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ApiResult<?> methodArgumentNotValidExceptionExceptionHandler(MethodArgumentNotValidException ex) {
//        log.warn("[methodArgumentNotValidExceptionExceptionHandler]", ex);
//        Optional<FieldError> fieldError = Optional.ofNullable(ex.getBindingResult().getFieldError());
//        return ApiResult.failed(ErrorCodeEnum.BAD_REQUEST, String.format("请求参数不正确:%s", fieldError.map(FieldError::getDefaultMessage).orElse(null)));
//    }

    /**
     * 处理 SpringMVC 参数绑定不正确，本质上也是通过 Validator 校验
     */
    @ExceptionHandler(BindException.class)
    public ApiResult<?> bindExceptionHandler(BindException ex) {
        log.warn("[handleBindException]", ex);
        FieldError fieldError = ex.getFieldError();
        assert fieldError != null; // 断言，避免告警
        return ApiResult.failed(ErrorCodeEnum.BAD_REQUEST, String.format("请求参数不正确:%s", fieldError.getDefaultMessage()));
    }

    /**
     * 处理 @Valid 校验不通过产生的异常
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ApiResult<?> argumentNotValidHandler(MethodArgumentNotValidException ex) {
        log.warn("[methodArgumentNotValidException]", ex);
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(s -> s.getField() + s.getDefaultMessage()).collect(Collectors.joining(";"));
        return ApiResult.failed(ErrorCodeEnum.BAD_REQUEST, message);
//        Optional<FieldError> fieldError = Optional.ofNullable(ex.getBindingResult().getFieldError());
//        return ApiResult.failed(ErrorCodeEnum.BAD_REQUEST, String.format("请求参数不正确:%s", fieldError.map(FieldError::getField).orElse(null)));
    }

    /**
     * 处理 @Validated 校验不通过产生的异常
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ApiResult<?> constraintViolationExceptionHandler(ConstraintViolationException ex) {
        log.warn("[constraintViolationExceptionHandler]", ex);
        String message = ex.getConstraintViolations().stream()
                .map(val -> val.getPropertyPath() + val.getMessage()).collect(Collectors.joining(";"));
        return ApiResult.failed(ErrorCodeEnum.BAD_REQUEST, message);
//        ConstraintViolation<?> constraintViolation = ex.getConstraintViolations().iterator().next();
//        return ApiResult.failed(ErrorCodeEnum.BAD_REQUEST, String.format("请求参数不正确:%s", constraintViolation.getMessage()));
    }

    /**
     * 处理 Dubbo Consumer 本地参数校验时，抛出的 ValidationException 异常
     */
    @ExceptionHandler(value = ValidationException.class)
    public ApiResult<?> validationException(ValidationException ex) {
        log.warn("[constraintViolationExceptionHandler]", ex);
        // 无法拼接明细的错误信息，因为 Dubbo Consumer 抛出 ValidationException 异常时，是直接的字符串信息，且人类不可读
        return ApiResult.failed(ErrorCodeEnum.BAD_REQUEST);
    }

    /**
     * 处理 SpringMVC 请求地址不存在
     *
     * 注意，它需要设置如下两个配置项：
     * 1. spring.mvc.throw-exception-if-no-handler-found 为 true
     * 2. spring.mvc.static-path-pattern 为 /statics/**
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ApiResult<?> noHandlerFoundExceptionHandler(HttpServletRequest req, NoHandlerFoundException ex) {
        log.warn("[noHandlerFoundExceptionHandler]", ex);
        return ApiResult.failed(ErrorCodeEnum.NOT_FOUND_REQUEST, String.format("请求地址不存在:%s", ex.getRequestURL()));
    }

    /**
     * 处理 SpringMVC 请求方法不正确
     *
     * 例如说，A 接口的方法为 GET 方式，结果请求方法为 POST 方式，导致不匹配
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ApiResult<?> httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException ex) {
        log.warn("[httpRequestMethodNotSupportedExceptionHandler]", ex);
        return ApiResult.failed(ErrorCodeEnum.METHOD_NOT_ALLOWED_REQUEST, String.format("请求方法不正确:%s", ex.getMessage()));
    }

    /**
     * 处理 Spring Security 权限不足的异常
     *
     * 来源是，使用 @PreAuthorize 注解，AOP 进行权限拦截
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    public ApiResult<?> accessDeniedExceptionHandler(HttpServletRequest req, AccessDeniedException ex) {
        log.warn("[accessDeniedExceptionHandler][userId({}) 无法访问 url({})]", RequestContext.getUserId(),
                req.getRequestURL(), ex);
        return ApiResult.failed(ErrorCodeEnum.FORBIDDEN);
    }

    /**
     * 处理业务异常 参见 {@link BusinessException}
     *
     * 例如说，商品库存不足，用户手机号已存在。
     */
    @ExceptionHandler(value = BusinessException.class)
    public ApiResult<?> serviceExceptionHandler(BusinessException ex) {
        log.info("[businessExceptionHandler]", ex);
        return ApiResult.failed(ex.getCode(), ex.getMessage());
    }

    /**
     * 处理系统异常，兜底处理所有的一切
     */
    @ExceptionHandler(value = Exception.class)
    public ApiResult<?> defaultExceptionHandler(HttpServletRequest req, Throwable ex) {
        log.error("[defaultExceptionHandler]", ex);
        return ApiResult.failed(ErrorCodeEnum.INTERNAL_SERVER_ERROR);
    }
}
