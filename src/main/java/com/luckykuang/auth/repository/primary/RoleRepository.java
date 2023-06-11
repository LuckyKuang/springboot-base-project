/*
 * Copyright 2015-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.luckykuang.auth.repository.primary;

import com.luckykuang.auth.model.primary.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

/**
 * @author luckykuang
 * @date 2023/4/20 14:16
 */
@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByCodeOrName(String code, String name);

    Optional<Role> findByIdIsNotAndCodeOrName(Long id, String code, String name);

    /**
     * data_scope 值越小，权限越大
     * 参见 {@link Role} 中的dateScope字段注释
     * @param roleIds
     * @return
     */
    @Query(
            nativeQuery = true,
            value = "select min(r.data_scope) from ky_role r where r.id in (:roleIds)"
    )
    Integer findMaxDataScope(@Param("roleIds") Set<Long> roleIds);
}
