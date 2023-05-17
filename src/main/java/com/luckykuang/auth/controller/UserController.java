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
import com.luckykuang.auth.model.User;
import com.luckykuang.auth.request.UserReq;
import com.luckykuang.auth.service.UserService;
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
 * @date 2023/4/20 15:40
 */
@Tag(name = "用户 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/v1/user")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {
    private final UserService userService;


    @Operation(summary = "分页查询用户列表",security = @SecurityRequirement(name = "Authorization"))
    @GetMapping("page")
    public ApiResult<PageResultVo<User>> queryUsersByPage(PageVo page){
        return ApiResult.success(userService.getUserByPage(page));
    }

    @Operation(summary = "查询用户列表",security = @SecurityRequirement(name = "Authorization"))
    @GetMapping
    public ApiResult<List<User>> queryUsers(){
        return ApiResult.success(userService.getUsers());
    }

    @Operation(summary = "通过id查询用户",security = @SecurityRequirement(name = "Authorization"))
    @GetMapping("{userId}")
    public ApiResult<User> queryUserById(@PathVariable("userId") Long id){
        return ApiResult.success(userService.getUserById(id));
    }

    @Operation(summary = "新增用户",security = @SecurityRequirement(name = "Authorization"))
    @PostMapping
    public ApiResult<User> insertUser(@RequestBody @Validated UserReq userReq){
        return ApiResult.success(userService.addUser(userReq));
    }

    @Operation(summary = "更新用户",security = @SecurityRequirement(name = "Authorization"))
    @PutMapping("{userId}")
    public ApiResult<User> updateUser(@PathVariable("userId") Long id,
                                      @RequestBody @Validated UserReq userReq){
        return ApiResult.success(userService.updateUser(id, userReq));
    }

    @Operation(summary = "删除用户",security = @SecurityRequirement(name = "Authorization"))
    @DeleteMapping("{userId}")
    public ApiResult<Long> deleteUser(@PathVariable("userId") Long id){
        userService.delUser(id);
        return ApiResult.success(id);
    }
}
