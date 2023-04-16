package com.luckykuang.auth.repository;

import com.luckykuang.auth.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author luckykuang
 * @date 2023/4/11 16:38
 */
@Repository
public interface RoleRepository extends JpaRepository<Roles,Long> {
    Optional<Roles> findByRoleName(String roleName);

    Optional<Roles> findByRoleIdIsNotAndRoleName(Long roleId, String roleName);
}
