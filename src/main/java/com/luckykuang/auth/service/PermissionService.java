package com.luckykuang.auth.service;

import com.luckykuang.auth.model.Permissions;
import com.luckykuang.auth.model.RolePermission;
import com.luckykuang.auth.model.RolePermissionId;
import com.luckykuang.auth.vo.PageVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

/**
 * @author luckykuang
 * @date 2023/4/11 16:37
 */
public interface PermissionService {

    Page<Permissions> queryPermissionsByPage(PageVo page);

    List<Permissions> queryPermissions();

    Optional<Permissions> queryPermissionById(Long permissionId);

    Permissions insertPermission(Permissions permissions);

    Permissions updatePermission(Permissions permissions);

    void deletePermission(Long permissionId);

    RolePermission authRolePermission(RolePermissionId rolePermissionId);
}
