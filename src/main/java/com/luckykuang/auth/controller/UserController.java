package com.luckykuang.auth.controller;

import com.luckykuang.auth.model.Users;
import com.luckykuang.auth.utils.ApiResult;
import com.luckykuang.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author luckykuang
 * @date 2023/4/11 16:27
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "auth/v1/user")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ApiResult<List<Users>> queryUsers(){
        return ApiResult.success(userService.queryUsers());
    }
    @GetMapping(path = "{userId}")
    public ApiResult<Optional<Users>> queryUserById(@PathVariable("userId") Long userId){
        return ApiResult.success(userService.queryUserById(userId));
    }
    @PostMapping
    public ApiResult<Users> insertUser(@RequestBody @Validated Users users){
        return ApiResult.success(userService.insertUser(users));
    }
    @PutMapping
    public ApiResult<Users> updateUser(@RequestBody @Validated Users users){
        return ApiResult.success(userService.updateUser(users));
    }
    @DeleteMapping(path = "{userId}")
    public ApiResult<Void> deleteUser(@PathVariable("userId") Long userId){
        userService.deleteUser(userId);
        return ApiResult.success();
    }
}
