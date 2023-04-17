package com.luckykuang.auth.service;

import com.luckykuang.auth.model.Roles;
import com.luckykuang.auth.model.UserRole;
import com.luckykuang.auth.model.UserRoleId;
import com.luckykuang.auth.vo.PageVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

/**
 * @author luckykuang
 * @date 2023/4/11 16:36
 */
public interface RoleService {

    Page<Roles> queryRolesByPage(PageVo page);

    List<Roles> queryRoles();

    Optional<Roles> queryRoleById(Long roleId);

    Roles insertRole(Roles roles);

    Roles updateRole(Roles roles);

    void deleteRole(Long roleId);

    UserRole authUserRole(UserRoleId userRoleId);
}
