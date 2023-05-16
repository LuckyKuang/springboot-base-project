package com.luckykuang.auth.repository;

import com.luckykuang.auth.model.Dept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author luckykuang
 * @date 2023/5/16 22:23
 */
@Repository
public interface DeptRepository extends JpaRepository<Dept,Long> {
}
