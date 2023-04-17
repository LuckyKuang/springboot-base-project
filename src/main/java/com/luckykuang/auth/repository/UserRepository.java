package com.luckykuang.auth.repository;

import com.luckykuang.auth.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author luckykuang
 * @date 2023/4/11 16:38
 */
@Repository
public interface UserRepository extends JpaRepository<Users,Long> {
    Optional<Users> findByUsername(String userName);

    Optional<Users> findByIdIsNotAndUsername(Long userId, String userName);

    Optional<Users> findByPhone(String phone);
}
