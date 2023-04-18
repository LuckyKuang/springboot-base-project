package com.luckykuang.auth.controller;

import com.luckykuang.auth.model.Users;
import com.luckykuang.auth.utils.ApiResult;
import com.luckykuang.auth.service.UserService;
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
 * @date 2023/4/11 16:27
 */

@Tag(name = "UserController", description = "用户")
@RequiredArgsConstructor
@RestController
@RequestMapping("auth/v1/user")
public class UserController {

    private final UserService userService;


    @Operation(summary = "分页查询用户列表")
    @GetMapping("page")
    public ApiResult<Page<Users>> queryUsersByPage(PageVo page){
        return ApiResult.success(userService.queryUsersByPage(page));
    }

    @Operation(summary = "查询用户列表")
    @GetMapping
    public ApiResult<List<Users>> queryUsers(){
        return ApiResult.success(userService.queryUsers());
    }

    @Operation(summary = "通过id查询用户")
    @GetMapping("{userId}")
    public ApiResult<Optional<Users>> queryUserById(@PathVariable("userId") Long userId){
        return ApiResult.success(userService.queryUserById(userId));
    }

    @Operation(summary = "新增用户")
    @PostMapping
    public ApiResult<Users> insertUser(@RequestBody @Validated Users users){
        return ApiResult.success(userService.insertUser(users));
    }

    @Operation(summary = "更新用户")
    @PutMapping
    public ApiResult<Users> updateUser(@RequestBody @Validated Users users){
        return ApiResult.success(userService.updateUser(users));
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("{userId}")
    public ApiResult<Void> deleteUser(@PathVariable("userId") Long userId){
        userService.deleteUser(userId);
        return ApiResult.success();
    }
}
