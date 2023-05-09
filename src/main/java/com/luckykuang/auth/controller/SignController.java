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
import com.luckykuang.auth.record.JwtRspRec;
import com.luckykuang.auth.record.SignInRec;
import com.luckykuang.auth.record.SignOnRec;
import com.luckykuang.auth.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author luckykuang
 * @date 2023/4/18 16:46
 */
@Tag(name = "认证 API")
@RestController
@RequestMapping("auth/v1/sign")
@RequiredArgsConstructor
public class SignController {

    private final UserService userService;

    @Operation(summary = "登录")
    @PostMapping("signIn")
    public JwtRspRec signIn(@RequestBody @Validated SignInRec sign){
        return userService.signIn(sign);
    }

    @Operation(summary = "注册")
    @PostMapping("signOn")
    public ApiResult<Void> signOn(@RequestBody @Validated SignOnRec sign){
        return userService.signOn(sign);
    }

    @Operation(summary = "刷新令牌")
    @PostMapping("refresh")
    public JwtRspRec refresh(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return userService.refresh(request,response);
    }
}
