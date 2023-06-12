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

import com.luckykuang.auth.base.ApiResult;
import com.luckykuang.auth.constants.enums.ErrorCode;
import com.luckykuang.auth.exception.BusinessException;
import com.luckykuang.auth.model.primary.Role;
import com.luckykuang.auth.repository.primary.RoleRepository;
import com.luckykuang.auth.service.RoleService;
import com.luckykuang.auth.utils.AssertUtils;
import com.luckykuang.auth.utils.PageUtils;
import com.luckykuang.auth.vo.PageResultVo;
import com.luckykuang.auth.vo.PageVo;
import com.luckykuang.auth.vo.request.RoleReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author luckykuang
 * @date 2023/4/20 14:19
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
//    @Transactional(rollbackFor = Exception.class)
    public Role addRole(RoleReq roleReq) {
        RoleReq from = RoleReq.from(roleReq);
        Optional<Role> roleOptional = roleRepository.findByCodeOrName(from.code(), from.name());
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
//    @Transactional(rollbackFor = Exception.class)
    public Role updateRole(Long id, RoleReq roleReq) {
        RoleReq from = RoleReq.from(roleReq);
        Optional<Role> repositoryById = roleRepository.findById(id);
        AssertUtils.isTrue(repositoryById.isPresent(), ErrorCode.ID_NOT_EXIST);
        Optional<Role> roleOptional = roleRepository.findByIdIsNotAndCodeOrName(id, from.code(), from.name());
        AssertUtils.isTrue(roleOptional.isEmpty(), ErrorCode.NAME_EXIST);
        return saveRole(id, from);
    }

    private Role saveRole(Long id, RoleReq roleReq) {
        Role role = new Role();
        role.setId(id);
        role.setCode(roleReq.code());
        role.setName(roleReq.name());
        role.setSort(roleReq.sort());
        role.setStatus(roleReq.status());
        role.setDescription(roleReq.description());
        return roleRepository.save(role);
    }

    @Override
    public ApiResult<Void> delRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ID_NOT_EXIST));
        roleRepository.delete(role);
        return ApiResult.success();
    }
}
