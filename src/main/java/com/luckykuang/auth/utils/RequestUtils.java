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

package com.luckykuang.auth.utils;

import com.luckykuang.auth.constants.enums.ErrorCode;
import com.luckykuang.auth.exception.BusinessException;
import com.luckykuang.auth.model.Menu;
import com.luckykuang.auth.security.userdetails.LoginUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.luckykuang.auth.constants.CoreConstants.BEARER_HEAD;
import static com.luckykuang.auth.constants.CoreConstants.ROLE_UNDERLINE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * 请求处理工具类
 * @author luckykuang
 * @date 2023/5/15 14:32
 */
public class RequestUtils {
    private RequestUtils(){}

    /**
     * 将请求头的token截取返回
     */
    public static Optional<String> resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_HEAD)) {
            return Optional.of(bearerToken.substring(BEARER_HEAD.length()));
        }
        return Optional.empty();
    }

    /**
     * 获取当前登录人信息
     * @return LoginUserDetails
     */
    public static Optional<LoginUserDetails> getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof LoginUserDetails) {
                return Optional.of((LoginUserDetails) authentication.getPrincipal());
            }
        }
        return Optional.empty();
    }

    /**
     * 获取用户ID
     * @return User
     */
    public static Long getUserId() {
        return getUser().orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_EXIST)).getUserId();
    }

    /**
     * 获取用户名
     * @return username
     */
    public static String getUsername() {
        return getUser().orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_EXIST)).getUsername();
    }

    /**
     * 获取用户名
     * @return username
     */
    public static String getPassword() {
        return getUser().orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_EXIST)).getPassword();
    }

    /**
     * 获取部门ID
     * @return deptId
     */
    public static Optional<Long> getDeptId() {
        return Optional.ofNullable(getUser().orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_EXIST)).getDeptId());
    }

    /**
     * 获取数据权限
     * @return dataScope
     */
    public static Optional<Integer> getDataScope() {
        return Optional.ofNullable(getUser().orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_EXIST)).getDataScope());
    }


    /**
     * 获取用户角色
     * @return roles
     */
    public static Optional<Set<String>> getRoles() {
        Collection<? extends GrantedAuthority> authorities = getUser()
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_EXIST)).getAuthorities();
        if (!CollectionUtils.isEmpty(authorities)){
            Set<String> roles = authorities.stream()
                    .filter(item -> item.getAuthority().startsWith(ROLE_UNDERLINE))
                    .map(item -> item.getAuthority().substring(ROLE_UNDERLINE.length()))
                    .collect(Collectors.toSet());
            return Optional.of(roles);
        }
        return Optional.empty();
    }

    /**
     * 获取用户菜单权限
     * @return menus
     */
    public static Optional<Set<String>> getMenus() {
        Set<Menu> menus = getUser().orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_EXIST)).getMenus();
        if (!CollectionUtils.isEmpty(menus)){
            Set<String> menusPerms = menus.stream().map(Menu::getPermissionName).collect(Collectors.toSet());
            return Optional.of(menusPerms);
        }
        return Optional.empty();
    }
}
