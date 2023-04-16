package com.luckykuang.auth.service;

import com.luckykuang.auth.model.Permissions;

import java.util.List;
import java.util.Optional;

/**
 * @author luckykuang
 * @date 2023/4/11 16:37
 */
public interface PermissionService {
    List<Permissions> queryPermissions();

    Optional<Permissions> queryPermissionById(Long permissionId);

    Permissions insertPermission(Permissions permissions);

    Permissions updatePermission(Permissions permissions);

    void deletePermission(Long permissionId);
}
