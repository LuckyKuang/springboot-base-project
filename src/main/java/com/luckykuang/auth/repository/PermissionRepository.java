package com.luckykuang.auth.repository;

import com.luckykuang.auth.model.Permissions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author luckykuang
 * @date 2023/4/11 16:38
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permissions,Long> {
    Optional<Permissions> findByPermissionName(String permissionName);

    Optional<Permissions> findByIdIsNotAndPermissionName(Long permissionId, String permissionName);
}
