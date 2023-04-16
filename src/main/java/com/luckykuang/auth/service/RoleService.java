package com.luckykuang.auth.service;

import com.luckykuang.auth.model.Roles;

import java.util.List;
import java.util.Optional;

/**
 * @author luckykuang
 * @date 2023/4/11 16:36
 */
public interface RoleService {
    List<Roles> queryRoles();

    Optional<Roles> queryRoleById(Long roleId);

    Roles insertRole(Roles roles);

    Roles updateRole(Roles roles);

    void deleteRole(Long roleId);
}
