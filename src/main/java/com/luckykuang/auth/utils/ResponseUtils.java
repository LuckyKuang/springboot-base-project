package com.luckykuang.auth.utils;

import com.google.gson.Gson;
import com.luckykuang.auth.base.ApiResult;
import com.luckykuang.auth.enums.ErrorCode;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;

/**
 * 响应处理工具类
 * @author luckykuang
 * @date 2023/5/17 00:07
 */
public class ResponseUtils {
    private ResponseUtils(){}

    /**
     * Security 异常消息返回处理
     * @param response
     * @param errorCode
     */
    public static void writeErrMsg(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        switch (errorCode) {
            case UNAUTHORIZED,TOKEN_INVALID -> response.setStatus(HttpStatus.UNAUTHORIZED.value());
            case FORBIDDEN -> response.setStatus(HttpStatus.FORBIDDEN.value());
            default -> response.setStatus(HttpStatus.BAD_REQUEST.value());
        }
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(new Gson().toJson(ApiResult.failed(errorCode)));
    }
}
