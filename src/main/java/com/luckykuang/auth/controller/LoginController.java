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
import com.luckykuang.auth.response.CaptchaRsp;
import com.luckykuang.auth.response.TokenRsp;
import com.luckykuang.auth.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    /**
     * 验证码信息通过请求头传入 参见 {@link com.luckykuang.auth.security.filter.JwtAuthenticationFilter}
     * @param loginReq 只传入username和password
     * @return
     */
    @Operation(summary = "登录")
    @PostMapping("login")
    public ApiResult<TokenRsp> login(@RequestBody @Validated LoginReq loginReq){
        return loginService.login(loginReq);
    }

    @Operation(summary = "退出登录",security = @SecurityRequirement(name = "Authorization"))
    @DeleteMapping("logout")
    public ApiResult<Void> logout(HttpServletRequest request){
        return loginService.logout(request);
    }

    @Operation(summary = "刷新令牌",security = @SecurityRequirement(name = "Authorization"))
    @PostMapping("refresh")
    public ApiResult<TokenRsp> refresh(HttpServletRequest request) {
        return loginService.refresh(request);
    }

    @Operation(summary = "获取验证码")
    @GetMapping("getCaptcha")
    public ApiResult<CaptchaRsp> getCaptcha() {
        return ApiResult.success(loginService.getCaptcha());
    }
}
