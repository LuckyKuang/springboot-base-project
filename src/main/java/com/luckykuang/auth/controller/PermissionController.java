package com.luckykuang.auth.controller;

import com.luckykuang.auth.model.Permissions;
import com.luckykuang.auth.model.RolePermission;
import com.luckykuang.auth.model.RolePermissionId;
import com.luckykuang.auth.service.PermissionService;
import com.luckykuang.auth.utils.ApiResult;
import com.luckykuang.auth.vo.PageVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author luckykuang
 * @date 2023/4/11 16:35
 */
@Tag(name = "PermissionController", description = "权限")
@RequiredArgsConstructor
@RestController
@RequestMapping("auth/v1/permission")
public class PermissionController {

    private final PermissionService permissionService;


    @Operation(summary = "分页查询权限列表")
    @GetMapping("page")
    public ApiResult<Page<Permissions>> queryPermissionsByPage(PageVo page){
        return ApiResult.success(permissionService.queryPermissionsByPage(page));
    }

    @Operation(summary = "查询权限列表")
    @GetMapping
    public ApiResult<List<Permissions>> queryPermissions(){
        return ApiResult.success(permissionService.queryPermissions());
    }

    @Operation(summary = "通过id查询权限")
    @GetMapping("{permissionId}")
    public ApiResult<Optional<Permissions>> queryPermissionById(@PathVariable("permissionId") Long permissionId){
        return ApiResult.success(permissionService.queryPermissionById(permissionId));
    }

    @Operation(summary = "新增权限")
    @PostMapping
    public ApiResult<Permissions> insertPermission(@RequestBody @Validated Permissions permissions){
        return ApiResult.success(permissionService.insertPermission(permissions));
    }

    @Operation(summary = "更新权限")
    @PutMapping
    public ApiResult<Permissions> updatePermission(@RequestBody @Validated Permissions permissions){
        return ApiResult.success(permissionService.updatePermission(permissions));
    }

    @Operation(summary = "删除权限")
    @DeleteMapping("{permissionId}")
    public ApiResult<Void> deletePermission(@PathVariable("permissionId") Long permissionId){
        permissionService.deletePermission(permissionId);
        return ApiResult.success();
    }

    @Operation(summary = "授予角色权限")
    @PostMapping("authRolePermission")
    public ApiResult<RolePermission> authRolePermission(@RequestBody RolePermissionId rolePermissionId){
        return ApiResult.success(permissionService.authRolePermission(rolePermissionId));
    }

}
