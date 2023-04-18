package com.luckykuang.auth.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

/**
 * @author luckykuang
 * @date 2023/4/18 00:09
 */
public record UserInfoUserDetails(String username,
                                  String password,
                                  Set<? extends GrantedAuthority> grantedAuthorities,
                                  boolean isAccountNonExpired,
                                  boolean isAccountNonLocked,
                                  boolean isCredentialsNonExpired,
                                  boolean isEnabled) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
