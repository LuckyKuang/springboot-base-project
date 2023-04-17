package com.luckykuang.auth.repository;

import com.luckykuang.auth.model.UserRole;
import com.luckykuang.auth.model.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author luckykuang
 * @date 2023/4/17 15:05
 */
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
}
