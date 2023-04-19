package com.luckykuang.auth.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

/**
 * 全局前置处理器
 * @author luckykuang
 * @date 2023/4/12 16:27
 */
@Component
public class GlobalInterceptor implements HandlerInterceptor {

    private static final String REQUEST_HEADER_TENANT_ID = "tenant_id";
    private static final String REQUEST_HEADER_USER_ID = "user_id";
    private static final String REQUEST_HEADER_ROLE_ID = "role_id";
    private static final String REQUEST_HEADER_LANGUAGE = "language";
    private static final String REQUEST_HEADER_AUTHORIZATION = "Authorization";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String tenantId = request.getHeader(REQUEST_HEADER_TENANT_ID);
        Long userId = Optional.ofNullable(request.getHeader(REQUEST_HEADER_USER_ID)).map(Long::valueOf).orElse(null);
        Long roleId = Optional.ofNullable(request.getHeader(REQUEST_HEADER_ROLE_ID)).map(Long::valueOf).orElse(null);
        // TODO 后期国际化/token加密
        String language = request.getHeader(REQUEST_HEADER_LANGUAGE);
        String token = request.getHeader(REQUEST_HEADER_AUTHORIZATION);
//        if (CommonUtils.isEmpty(tenantId,userId,roleId)){
//            // TODO
//            return false;
//        }
        RequestContext.setTenantId(tenantId);
        RequestContext.setUserId(userId);
        RequestContext.setRoleId(roleId);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        RequestContext.clear();
    }
}
