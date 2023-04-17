package com.luckykuang.auth.controller;

import com.luckykuang.auth.model.Permissions;
import com.luckykuang.auth.model.RolePermission;
import com.luckykuang.auth.model.RolePermissionId;
import com.luckykuang.auth.service.PermissionService;
import com.luckykuang.auth.utils.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author luckykuang
 * @date 2023/4/11 16:35
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "auth/v1/permission")
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping
    public ApiResult<List<Permissions>> queryPermissions(){
        return ApiResult.success(permissionService.queryPermissions());
    }
    @GetMapping(path = "{permissionId}")
    public ApiResult<Optional<Permissions>> queryPermissionById(@PathVariable("permissionId") Long permissionId){
        return ApiResult.success(permissionService.queryPermissionById(permissionId));
    }
    @PostMapping
    public ApiResult<Permissions> insertPermission(@RequestBody @Validated Permissions permissions){
        return ApiResult.success(permissionService.insertPermission(permissions));
    }
    @PutMapping
    public ApiResult<Permissions> updatePermission(@RequestBody @Validated Permissions permissions){
        return ApiResult.success(permissionService.updatePermission(permissions));
    }
    @DeleteMapping(path = "{permissionId}")
    public ApiResult<Void> deletePermission(@PathVariable("permissionId") Long permissionId){
        permissionService.deletePermission(permissionId);
        return ApiResult.success();
    }

    @PostMapping(path = "authRolePermission")
    public ApiResult<RolePermission> authRolePermission(@RequestBody RolePermissionId rolePermissionId){
        return ApiResult.success(permissionService.authRolePermission(rolePermissionId));
    }

}
