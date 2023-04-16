package com.luckykuang.auth.config;

import java.util.Optional;

/**
 * 请求信息捕获
 * @author luckykuang
 * @date 2023/4/10 19:31
 */
public class RequestContext {

    private static final ThreadLocal<String> currentTenant = new ThreadLocal<>();
    private static final ThreadLocal<Long> currentUser = new ThreadLocal<>();
    private static final ThreadLocal<Long> currentRole = new ThreadLocal<>();


    /**
     * 设置租户id
     */
    public static void setTenantId(String tenantId) {
        currentTenant.set(tenantId);
    }

    /**
     * 获取租户id
     */
    public static Optional<String> getTenantId() {
        return Optional.ofNullable(currentTenant.get());
    }

    /**
     * 设置用户id
     */
    public static void setUserId(Long userId) {
        currentUser.set(userId);
    }

    /**
     * 获取用户id
     */
    public static Optional<Long> getUserId() {
        return Optional.ofNullable(currentUser.get());
    }

    /**
     * 设置角色id
     */
    public static void setRoleId(Long roleId) {
        currentRole.set(roleId);
    }

    /**
     * 获取角色id
     */
    public static Optional<Long> getRoleId() {
        return Optional.ofNullable(currentRole.get());
    }

    public static void clear() {
        currentTenant.remove();
        currentUser.remove();
        currentRole.remove();
    }
}
