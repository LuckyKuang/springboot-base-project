package com.luckykuang.auth.repository;

import com.luckykuang.auth.model.RolePermission;
import com.luckykuang.auth.model.RolePermissionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author luckykuang
 * @date 2023/4/17 14:49
 */
@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, RolePermissionId> {
}
