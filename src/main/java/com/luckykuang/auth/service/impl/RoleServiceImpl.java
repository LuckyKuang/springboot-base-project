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

package com.luckykuang.auth.service.impl;

import com.luckykuang.auth.enums.ErrorCode;
import com.luckykuang.auth.exception.BusinessException;
import com.luckykuang.auth.model.Permission;
import com.luckykuang.auth.model.Role;
import com.luckykuang.auth.record.RoleRec;
import com.luckykuang.auth.repository.PermissionRepository;
import com.luckykuang.auth.repository.RoleRepository;
import com.luckykuang.auth.service.RoleService;
import com.luckykuang.auth.utils.AssertUtils;
import com.luckykuang.auth.utils.PageUtils;
import com.luckykuang.auth.vo.PageResultVo;
import com.luckykuang.auth.vo.PageVo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author luckykuang
 * @date 2023/4/20 14:19
 */
@Service
@Transactional
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    public Role addRole(RoleRec roleRec) {
        RoleRec from = RoleRec.from(roleRec);
        Optional<Role> roleOptional = roleRepository.findByRoleName(from.roleName());
        AssertUtils.isTrue(roleOptional.isEmpty(), ErrorCode.NAME_EXIST);
        return saveRole(null, from);
    }

    @Override
    public Role getRoleById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @Override
    public PageResultVo<Role> getRolesByPage(PageVo page) {
        Pageable pageable = PageUtils.getPageable(page, Sort.Direction.DESC, "updateTime");
        return PageUtils.getPageResult(roleRepository.findAll(pageable));
    }

    @Override
    public Role updateRole(Long id, RoleRec roleRec) {
        RoleRec from = RoleRec.from(roleRec);
        Optional<Role> repositoryById = roleRepository.findById(id);
        AssertUtils.isTrue(repositoryById.isPresent(), ErrorCode.ID_NOT_EXIST);
        Optional<Role> roleOptional = roleRepository.findByIdIsNotAndRoleName(id, from.roleName());
        AssertUtils.isTrue(roleOptional.isEmpty(), ErrorCode.NAME_EXIST);

        return saveRole(id, from);
    }

    private Role saveRole(Long id, RoleRec roleRec) {
        Optional<Permission> permission = permissionRepository.findById(roleRec.permissionId());
        if (permission.isEmpty()){
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        Role role = new Role();
        role.setId(id);
        role.setRoleName(roleRec.roleName());
        role.setDescription(roleRec.description());
        Role save = roleRepository.save(role);
        save.getPermissions().add(permission.get());
        return save;
    }

    @Override
    public void delRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ID_NOT_EXIST));
        roleRepository.delete(role);
    }
}
