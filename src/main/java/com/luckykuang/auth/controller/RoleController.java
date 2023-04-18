package com.luckykuang.auth.controller;

import com.luckykuang.auth.model.*;
import com.luckykuang.auth.utils.ApiResult;
import com.luckykuang.auth.service.RoleService;
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
 * @date 2023/4/11 16:34
 */
@Tag(name = "RoleController", description = "角色")
@RequiredArgsConstructor
@RestController
@RequestMapping("auth/v1/role")
public class RoleController {

    private final RoleService roleService;

    @Operation(summary = "分页查询角色列表")
    @GetMapping("page")
    public ApiResult<Page<Roles>> queryRolesByPage(PageVo page){
        return ApiResult.success(roleService.queryRolesByPage(page));
    }

    @Operation(summary = "查询角色列表")
    @GetMapping
    public ApiResult<List<Roles>> queryRoles(){
        return ApiResult.success(roleService.queryRoles());
    }

    @Operation(summary = "通过id查询角色")
    @GetMapping("{roleId}")
    public ApiResult<Optional<Roles>> queryRoleById(@PathVariable("roleId") Long roleId){
        return ApiResult.success(roleService.queryRoleById(roleId));
    }

    @Operation(summary = "新增角色")
    @PostMapping
    public ApiResult<Roles> insertRole(@RequestBody @Validated Roles roles){
        return ApiResult.success(roleService.insertRole(roles));
    }

    @Operation(summary = "更新角色")
    @PutMapping
    public ApiResult<Roles> updateRole(@RequestBody @Validated Roles roles){
        return ApiResult.success(roleService.updateRole(roles));
    }

    @Operation(summary = "删除角色")
    @DeleteMapping("{roleId}")
    public ApiResult<Void> deleteRole(@PathVariable("roleId") Long roleId){
        roleService.deleteRole(roleId);
        return ApiResult.success();
    }

    @Operation(summary = "授予用户角色")
    @PostMapping("authUserRole")
    public ApiResult<UserRole> authUserRole(@RequestBody UserRoleId userRoleId){
        return ApiResult.success(roleService.authUserRole(userRoleId));
    }

}
