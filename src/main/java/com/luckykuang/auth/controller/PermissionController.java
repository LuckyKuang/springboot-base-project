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
import com.luckykuang.auth.model.Permission;
import com.luckykuang.auth.request.PermissionReq;
import com.luckykuang.auth.service.PermissionService;
import com.luckykuang.auth.vo.PageResultVo;
import com.luckykuang.auth.vo.PageVo;
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
 * @date 2023/4/11 16:35
 */
@Tag(name = "权限 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("auth/v1/permission")
@PreAuthorize("hasRole('ADMIN')")
public class PermissionController {

    private final PermissionService permissionService;


    @Operation(summary = "分页查询权限列表",security = @SecurityRequirement(name = "Authorization"))
    @GetMapping("page")
    public ApiResult<PageResultVo<Permission>> getPermissionsByPage(PageVo page){
        return ApiResult.success(permissionService.getPermissionsByPage(page));
    }

    @Operation(summary = "查询权限列表",security = @SecurityRequirement(name = "Authorization"))
    @GetMapping
    public ApiResult<List<Permission>> getPermissions(){
        return ApiResult.success(permissionService.getPermissions());
    }

    @Operation(summary = "通过id查询权限",security = @SecurityRequirement(name = "Authorization"))
    @GetMapping("{permissionId}")
    public ApiResult<Permission> getPermissionById(@PathVariable("permissionId") Long id){
        return ApiResult.success(permissionService.getPermissionById(id));
    }

    @Operation(summary = "新增权限",security = @SecurityRequirement(name = "Authorization"))
    @PostMapping
    public ApiResult<Permission> addPermission(@RequestBody @Validated PermissionReq permissionReq){
        return ApiResult.success(permissionService.addPermission(permissionReq));
    }

    @Operation(summary = "更新权限",security = @SecurityRequirement(name = "Authorization"))
    @PutMapping("{permissionId}")
    public ApiResult<Permission> updatePermission(@PathVariable("permissionId") Long id,
                                                  @RequestBody @Validated PermissionReq permissionReq){
        return ApiResult.success(permissionService.updatePermission(id, permissionReq));
    }

    @Operation(summary = "删除权限",security = @SecurityRequirement(name = "Authorization"))
    @DeleteMapping("{permissionId}")
    public ApiResult<Long> delPermission(@PathVariable("permissionId") Long id){
        permissionService.delPermission(id);
        return ApiResult.success(id);
    }
}
