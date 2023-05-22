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
import com.luckykuang.auth.model.Menu;
import com.luckykuang.auth.request.MenuReq;
import com.luckykuang.auth.service.MenuService;
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
@Tag(name = "菜单权限 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("auth/v1/permission")
@PreAuthorize("hasRole('ADMIN')")
public class MenuController {

    private final MenuService menuService;


    @Operation(summary = "分页查询菜单权限列表",security = @SecurityRequirement(name = "Authorization"))
    @GetMapping("page")
    public ApiResult<PageResultVo<Menu>> getMenusByPage(PageVo page){
        return ApiResult.success(menuService.getMenusByPage(page));
    }

    @Operation(summary = "查询菜单权限列表",security = @SecurityRequirement(name = "Authorization"))
    @GetMapping
    public ApiResult<List<Menu>> getMenus(){
        return ApiResult.success(menuService.getMenus());
    }

    @Operation(summary = "通过id查询菜单权限",security = @SecurityRequirement(name = "Authorization"))
    @GetMapping("{menuId}")
    public ApiResult<Menu> getMenuById(@PathVariable("menuId") Long id){
        return ApiResult.success(menuService.getMenuById(id));
    }

    @Operation(summary = "新增菜单权限",security = @SecurityRequirement(name = "Authorization"))
    @PostMapping
    public ApiResult<Menu> addMenu(@RequestBody @Validated MenuReq menuReq){
        return ApiResult.success(menuService.addMenu(menuReq));
    }

    @Operation(summary = "更新菜单权限",security = @SecurityRequirement(name = "Authorization"))
    @PutMapping("{menuId}")
    public ApiResult<Menu> updateMenu(@PathVariable("menuId") Long id,
                                                  @RequestBody @Validated MenuReq menuReq){
        return ApiResult.success(menuService.updateMenu(id, menuReq));
    }

    @Operation(summary = "删除菜单权限",security = @SecurityRequirement(name = "Authorization"))
    @DeleteMapping("{menuId}")
    public ApiResult<Long> delMenu(@PathVariable("menuId") Long id){
        menuService.delMenu(id);
        return ApiResult.success(id);
    }
}
