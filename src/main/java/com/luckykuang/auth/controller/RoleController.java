package com.luckykuang.auth.controller;

import com.luckykuang.auth.model.*;
import com.luckykuang.auth.utils.ApiResult;
import com.luckykuang.auth.service.RoleService;
import com.luckykuang.auth.vo.PageVo;
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
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "auth/v1/role")
public class RoleController {

    private final RoleService roleService;

    @GetMapping(path = "page")
    public ApiResult<Page<Roles>> queryRolesByPage(PageVo page){
        return ApiResult.success(roleService.queryRolesByPage(page));
    }
    @GetMapping
    public ApiResult<List<Roles>> queryRoles(){
        return ApiResult.success(roleService.queryRoles());
    }
    @GetMapping(path = "{roleId}")
    public ApiResult<Optional<Roles>> queryRoleById(@PathVariable("roleId") Long roleId){
        return ApiResult.success(roleService.queryRoleById(roleId));
    }
    @PostMapping
    public ApiResult<Roles> insertRole(@RequestBody @Validated Roles roles){
        return ApiResult.success(roleService.insertRole(roles));
    }
    @PutMapping
    public ApiResult<Roles> updateRole(@RequestBody @Validated Roles roles){
        return ApiResult.success(roleService.updateRole(roles));
    }
    @DeleteMapping(path = "{roleId}")
    public ApiResult<Void> deleteRole(@PathVariable("roleId") Long roleId){
        roleService.deleteRole(roleId);
        return ApiResult.success();
    }

    @PostMapping(path = "authUserRole")
    public ApiResult<UserRole> authUserRole(@RequestBody UserRoleId userRoleId){
        return ApiResult.success(roleService.authUserRole(userRoleId));
    }

}
