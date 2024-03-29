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

package com.luckykuang.auth.controller;

import com.luckykuang.auth.base.ApiResult;
import com.luckykuang.auth.model.primary.Role;
import com.luckykuang.auth.service.RoleService;
import com.luckykuang.auth.vo.PageResultVo;
import com.luckykuang.auth.vo.PageVo;
import com.luckykuang.auth.vo.request.RoleReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author luckykuang
 * @date 2023/4/11 16:34
 */
@Tag(name = "角色 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("auth/v1/role")
@PreAuthorize("hasRole('ADMIN')")
public class RoleController {

    private final RoleService roleService;

    @Operation(summary = "分页查询角色列表",security = @SecurityRequirement(name = "Authorization"))
    @GetMapping("page")
    public ApiResult<PageResultVo<Role>> getRolesByPage(PageVo page){
        return ApiResult.success(roleService.getRolesByPage(page));
    }

    @Operation(summary = "查询角色列表",security = @SecurityRequirement(name = "Authorization"))
    @GetMapping
    public ApiResult<List<Role>> queryRoles(){
        return ApiResult.success(roleService.getRoles());
    }

    @Operation(summary = "通过id查询角色",security = @SecurityRequirement(name = "Authorization"))
    @GetMapping("{roleId}")
    public ApiResult<Role> queryRoleById(@PathVariable("roleId") Long id){
        return ApiResult.success(roleService.getRoleById(id));
    }

    @Operation(summary = "新增角色",security = @SecurityRequirement(name = "Authorization"))
    @PostMapping
    public ApiResult<Role> insertRole(@RequestBody @Validated RoleReq roleReq){
        return ApiResult.success(roleService.addRole(roleReq));
    }

    @Operation(summary = "更新角色",security = @SecurityRequirement(name = "Authorization"))
    @PutMapping("{roleId}")
    public ApiResult<Role> updateRole(@PathVariable("roleId") Long id,
                                      @RequestBody @Validated RoleReq roleReq){
        return ApiResult.success(roleService.updateRole(id, roleReq));
    }

    @Operation(summary = "删除角色",security = @SecurityRequirement(name = "Authorization"))
    @DeleteMapping("{roleId}")
    public ApiResult<Void> delRole(@PathVariable("roleId") Long id){
        return roleService.delRole(id);
    }
}
