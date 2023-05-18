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
import com.luckykuang.auth.request.LoginReq;
import com.luckykuang.auth.request.RefreshReq;
import com.luckykuang.auth.response.CaptchaRsp;
import com.luckykuang.auth.response.TokenRsp;
import com.luckykuang.auth.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author luckykuang
 * @date 2023/4/18 16:46
 */
@Tag(name = "认证 API")
@RestController
@RequestMapping("auth/v1/sign")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @Operation(summary = "登录")
    @GetMapping("login")
    public ApiResult<TokenRsp> login(LoginReq loginReq){
        return loginService.login(loginReq);
    }

    @Operation(summary = "退出登录")
    @DeleteMapping("logout")
    public ApiResult<Void> logout(HttpServletRequest request){
        return loginService.logout(request);
    }

    @Operation(summary = "刷新令牌")
    @PostMapping("refresh")
    public ApiResult<TokenRsp> refresh(@RequestBody @Validated RefreshReq refreshReq) {
        return loginService.refresh(refreshReq);
    }

    @Operation(summary = "获取验证码")
    @GetMapping("getCaptcha")
    public ApiResult<CaptchaRsp> getCaptcha() {
        return ApiResult.success(loginService.getCaptcha());
    }
}
