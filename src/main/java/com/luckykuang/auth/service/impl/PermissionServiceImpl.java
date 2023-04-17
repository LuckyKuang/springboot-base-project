package com.luckykuang.auth.service.impl;

import com.luckykuang.auth.constant.ErrorCodeEnum;
import com.luckykuang.auth.model.Permissions;
import com.luckykuang.auth.model.RolePermission;
import com.luckykuang.auth.model.RolePermissionId;
import com.luckykuang.auth.model.Roles;
import com.luckykuang.auth.repository.PermissionRepository;
import com.luckykuang.auth.repository.RolePermissionRepository;
import com.luckykuang.auth.repository.RoleRepository;
import com.luckykuang.auth.service.PermissionService;
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
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final RolePermissionRepository rolePermissionRepository;

    @Override
    public List<Permissions> queryPermissions() {
        return permissionRepository.findAll();
    }

    @Override
    public Optional<Permissions> queryPermissionById(Long permissionId) {
        return permissionRepository.findById(permissionId);
    }

    @Override
    public Permissions insertPermission(Permissions permissions) {
        Optional<Permissions> permissionsOptional = permissionRepository.findByPermissionName(permissions.getPermissionName());
        AssertUtils.isTrue(permissionsOptional.isEmpty(), ErrorCodeEnum.NAME_DUPLICATION);

        return permissionRepository.save(permissions);
    }

    @Override
    public Permissions updatePermission(Permissions permissions) {
        Optional<Permissions> repositoryById = permissionRepository.findById(permissions.getPermissionId());
        AssertUtils.isTrue(repositoryById.isPresent(), ErrorCodeEnum.ID_NOT_EXIST);
        Optional<Permissions> permissionsOptional = permissionRepository
                .findByPermissionIdIsNotAndPermissionName(permissions.getPermissionId(), permissions.getPermissionName());
        AssertUtils.isTrue(permissionsOptional.isEmpty(), ErrorCodeEnum.NAME_DUPLICATION);

        return permissionRepository.save(permissions);
    }

    @Override
    public void deletePermission(Long permissionId) {
        permissionRepository.deleteById(permissionId);
    }

    @Override
    public RolePermission authRolePermission(RolePermissionId rolePermissionId) {
        Optional<Permissions> permissions = permissionRepository.findById(rolePermissionId.getPermissionId());
        Optional<Roles> roles = roleRepository.findById(rolePermissionId.getRoleId());
        AssertUtils.isTrue(permissions.isPresent() && roles.isPresent(),ErrorCodeEnum.BAD_REQUEST);

        RolePermission rolePermission = new RolePermission();
        rolePermission.setId(rolePermissionId);
        rolePermission.setRolePermissionFk(roles.get());
        rolePermission.setPermissionRoleFk(permissions.get());
        return rolePermissionRepository.save(rolePermission);
    }
}
