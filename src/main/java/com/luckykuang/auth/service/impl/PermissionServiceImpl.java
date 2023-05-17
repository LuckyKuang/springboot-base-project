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

import com.luckykuang.auth.constants.enums.ErrorCode;
import com.luckykuang.auth.exception.BusinessException;
import com.luckykuang.auth.model.Permission;
import com.luckykuang.auth.repository.PermissionRepository;
import com.luckykuang.auth.request.PermissionReq;
import com.luckykuang.auth.service.PermissionService;
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
 * @date 2023/4/20 14:18
 */
@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Permission addPermission(PermissionReq permissionReq) {
        PermissionReq from = PermissionReq.from(permissionReq);
        Optional<Permission> permissionsOptional = permissionRepository
                .findByCodeOrName(from.code(), from.name());
        AssertUtils.isTrue(permissionsOptional.isEmpty(), ErrorCode.NAME_EXIST);
        return savePermission(null, from);
    }

    @Override
    public Permission getPermissionById(Long id) {
        return permissionRepository.findById(id).orElse(null);
    }

    @Override
    public List<Permission> getPermissions() {
        return permissionRepository.findAll();
    }

    @Override
    public PageResultVo<Permission> getPermissionsByPage(PageVo page) {
        Pageable pageable = PageUtils.getPageable(page, Sort.Direction.DESC, "updateTime");
        return PageUtils.getPageResult(permissionRepository.findAll(pageable));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Permission updatePermission(Long id, PermissionReq permissionReq) {
        PermissionReq from = PermissionReq.from(permissionReq);
        Optional<Permission> repositoryById = permissionRepository.findById(id);
        AssertUtils.isTrue(repositoryById.isPresent(), ErrorCode.ID_NOT_EXIST);
        Optional<Permission> permissionsOptional = permissionRepository
                .findByIdIsNotAndCodeOrName(id, from.code(), from.name());
        AssertUtils.isTrue(permissionsOptional.isEmpty(), ErrorCode.NAME_EXIST);

        return savePermission(id, from);
    }

    private Permission savePermission(Long id, PermissionReq permissionReq) {
        Permission permission = new Permission();
        permission.setId(id);
        permission.setCode(permissionReq.code());
        permission.setName(permissionReq.name());
        permission.setDescription(permissionReq.description());
        return permissionRepository.save(permission);
    }

    @Override
    public void delPermission(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ID_NOT_EXIST));
        permissionRepository.delete(permission);
    }
}
