package com.luckykuang.auth.security;

import com.luckykuang.auth.constant.ErrorCodeEnum;
import com.luckykuang.auth.exception.BusinessException;
import com.luckykuang.auth.model.*;
import com.luckykuang.auth.repository.PermissionRepository;
import com.luckykuang.auth.repository.RoleRepository;
import com.luckykuang.auth.repository.UserRepository;
import com.luckykuang.auth.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author luckykuang
 * @date 2023/4/18 00:05
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ErrorCodeEnum.FORBIDDEN));
        UserRole userRole = userRoleRepository.findUserRoleByUserRoleFk(users)
                .orElseThrow(() -> new BusinessException(ErrorCodeEnum.FORBIDDEN));
        return new LoginUser(
                users.getUsername(),
                users.getPassword(),
                getGrantedAuthorities(userRole.getUserRoleFk().getId()),
                true,
                true,
                true,
                true
        );
    }

    private Set<SimpleGrantedAuthority> getGrantedAuthorities(Long roleId) {
        Roles roles = roleRepository.findById(roleId)
                .orElseThrow(() -> new BusinessException(ErrorCodeEnum.FORBIDDEN));

        Set<SimpleGrantedAuthority> grantedAuthoritySet = permissionRepository.findAll().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermissionField()))
                .collect(Collectors.toSet());
        grantedAuthoritySet.add(new SimpleGrantedAuthority("ROLE_" + roles.getRoleField()));
        return grantedAuthoritySet;
    }
}
