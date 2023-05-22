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

package com.luckykuang.auth.security.userdetails;

import com.luckykuang.auth.model.Menu;
import com.luckykuang.auth.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author luckykuang
 * @date 2023/5/5 10:36
 */
@Getter
@Setter
@AllArgsConstructor
public class LoginUserDetails implements UserDetails {

    @Serial
    private static final long serialVersionUID = -8218935861136040887L;
    /**
     * 用户id
     */
    private Long userId;
    private String username;
    private String password;
    /**
     * 部门id
     */
    private Long deptId;
    /**
     * 数据权限 0-全部数据 1-本部门及子部门数据 2-本部门数据 3-本人数据
     */
    private Integer dataScope;
    /**
     * 角色
     */
    private Set<Role> roles;
    /**
     * 菜单权限
     */
    private Set<Menu> menus;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getCode()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
