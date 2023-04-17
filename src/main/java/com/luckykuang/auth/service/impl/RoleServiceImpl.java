package com.luckykuang.auth.service.impl;
import com.luckykuang.auth.model.Users;

import com.luckykuang.auth.constant.ErrorCodeEnum;
import com.luckykuang.auth.model.Roles;
import com.luckykuang.auth.model.UserRole;
import com.luckykuang.auth.model.UserRoleId;
import com.luckykuang.auth.repository.RoleRepository;
import com.luckykuang.auth.repository.UserRepository;
import com.luckykuang.auth.repository.UserRoleRepository;
import com.luckykuang.auth.service.RoleService;
import com.luckykuang.auth.utils.AssertUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author luckykuang
 * @date 2023/4/11 16:37
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    public List<Roles> queryRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Optional<Roles> queryRoleById(Long roleId) {
        return roleRepository.findById(roleId);
    }

    @Override
    public Roles insertRole(Roles roles) {
        Optional<Roles> rolesOptional = roleRepository.findByRoleName(roles.getRoleName());
        AssertUtils.isTrue(rolesOptional.isEmpty(), ErrorCodeEnum.NAME_DUPLICATION);

        return roleRepository.save(roles);
    }

    @Override
    public Roles updateRole(Roles roles) {
        Optional<Roles> repositoryById = roleRepository.findById(roles.getRoleId());
        AssertUtils.isTrue(repositoryById.isPresent(), ErrorCodeEnum.ID_NOT_EXIST);
        Optional<Roles> rolesOptional = roleRepository.findByRoleIdIsNotAndRoleName(roles.getRoleId(), roles.getRoleName());
        AssertUtils.isTrue(rolesOptional.isEmpty(), ErrorCodeEnum.NAME_DUPLICATION);

        return roleRepository.save(roles);
    }

    @Override
    public void deleteRole(Long roleId) {
        roleRepository.deleteById(roleId);
    }

    @Override
    public UserRole authUserRole(UserRoleId userRoleId) {
        Optional<Users> users = userRepository.findById(userRoleId.getUserId());
        Optional<Roles> roles = roleRepository.findById(userRoleId.getRoleId());
        AssertUtils.isTrue(users.isPresent() && roles.isPresent(),ErrorCodeEnum.BAD_REQUEST);

        UserRole userRole = new UserRole();
        userRole.setId(userRoleId);
        userRole.setUserRoleFk(users.get());
        userRole.setRoleUserFk(roles.get());
        return userRoleRepository.save(userRole);
    }
}
