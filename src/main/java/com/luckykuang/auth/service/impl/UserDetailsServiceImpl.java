package com.luckykuang.auth.service.impl;

import com.luckykuang.auth.constant.ErrorCodeEnum;
import com.luckykuang.auth.model.*;
import com.luckykuang.auth.repository.PermissionRepository;
import com.luckykuang.auth.repository.RoleRepository;
import com.luckykuang.auth.repository.UserRepository;
import com.luckykuang.auth.repository.UserRoleRepository;
import com.luckykuang.auth.utils.AssertUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author luckykuang
 * @date 2023/4/18 00:05
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> users = userRepository.findByUsername(username);
        AssertUtils.isTrue(users.isPresent(), ErrorCodeEnum.FORBIDDEN);
        UserRoleId id = new UserRoleId();
        id.setUserId(users.get().getId());
        Optional<UserRole> userRoleOptional = userRoleRepository.findUserRoleById(id);
        AssertUtils.isTrue(userRoleOptional.isPresent(), ErrorCodeEnum.FORBIDDEN);
        return new LoginUser(
                users.get().getUsername(),
                users.get().getPasswd(),
                getGrantedAuthorities(userRoleOptional.get().getUserRoleFk().getId()),
                true,
                true,
                true,
                true
        );
    }

    private Set<SimpleGrantedAuthority> getGrantedAuthorities(Long roleId) {
        Optional<Roles> roles = roleRepository.findById(roleId);
        AssertUtils.isTrue(roles.isPresent(), ErrorCodeEnum.FORBIDDEN);

        Set<SimpleGrantedAuthority> grantedAuthoritySet = permissionRepository.findAll().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermissionField()))
                .collect(Collectors.toSet());
        grantedAuthoritySet.add(new SimpleGrantedAuthority("ROLE_" + roles.get().getRoleField()));
        return grantedAuthoritySet;
    }
}
