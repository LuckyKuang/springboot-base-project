package com.luckykuang.auth.service.impl;

import com.luckykuang.auth.constant.ErrorCodeEnum;
import com.luckykuang.auth.exception.BusinessException;
import com.luckykuang.auth.model.Permissions;
import com.luckykuang.auth.model.RolePermission;
import com.luckykuang.auth.model.RolePermissionId;
import com.luckykuang.auth.model.Roles;
import com.luckykuang.auth.repository.PermissionRepository;
import com.luckykuang.auth.repository.RolePermissionRepository;
import com.luckykuang.auth.repository.RoleRepository;
import com.luckykuang.auth.service.PermissionService;
import com.luckykuang.auth.utils.AssertUtils;
import com.luckykuang.auth.utils.PageUtils;
import com.luckykuang.auth.vo.PageVo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public Page<Permissions> queryPermissionsByPage(PageVo page) {
        Pageable pageable = PageUtils.getPageable(page);
        return permissionRepository.findAll(pageable);
    }

    @Override
    public List<Permissions> queryPermissions() {
        return permissionRepository.findAll(Sort.by("updateTime"));
    }

    @Override
    public Optional<Permissions> queryPermissionById(Long permissionId) {
        return permissionRepository.findById(permissionId);
    }

    @Override
    public Permissions insertPermission(Permissions permissions) {
        Optional<Permissions> permissionsOptional = permissionRepository.findByPermissionName(permissions.getPermissionName());
        AssertUtils.isTrue(permissionsOptional.isEmpty(), ErrorCodeEnum.NAME_EXIST);

        return permissionRepository.save(permissions);
    }

    @Override
    public Permissions updatePermission(Permissions permissions) {
        Optional<Permissions> repositoryById = permissionRepository.findById(permissions.getId());
        AssertUtils.isTrue(repositoryById.isPresent(), ErrorCodeEnum.ID_NOT_EXIST);
        Optional<Permissions> permissionsOptional = permissionRepository
                .findByIdIsNotAndPermissionName(permissions.getId(), permissions.getPermissionName());
        AssertUtils.isTrue(permissionsOptional.isEmpty(), ErrorCodeEnum.NAME_EXIST);

        return permissionRepository.save(permissions);
    }

    @Override
    public void deletePermission(Long permissionId) {
        permissionRepository.deleteById(permissionId);
    }

    @Override
    public RolePermission authRolePermission(RolePermissionId rolePermissionId) {
        Permissions permissions = permissionRepository.findById(rolePermissionId.getPermissionId())
                .orElseThrow(() -> new BusinessException(ErrorCodeEnum.BAD_REQUEST));
        Roles roles = roleRepository.findById(rolePermissionId.getRoleId())
                .orElseThrow(() -> new BusinessException(ErrorCodeEnum.BAD_REQUEST));

        RolePermission rolePermission = new RolePermission();
        rolePermission.setId(rolePermissionId);
        rolePermission.setRolePermissionFk(roles);
        rolePermission.setPermissionRoleFk(permissions);
        return rolePermissionRepository.save(rolePermission);
    }
}
