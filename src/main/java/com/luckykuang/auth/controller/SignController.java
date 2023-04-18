package com.luckykuang.auth.controller;

import com.luckykuang.auth.record.JwtRspRec;
import com.luckykuang.auth.record.SignInRec;
import com.luckykuang.auth.record.SignOnRec;
import com.luckykuang.auth.service.SignService;
import com.luckykuang.auth.utils.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author luckykuang
 * @date 2023/4/18 16:46
 */
@Tag(name = "SignController", description = "登录注册")
@RestController
@RequestMapping("auth/v1/sign")
@RequiredArgsConstructor
public class SignController {

    private final SignService signService;

    @Operation(summary = "登录")
    @PostMapping("signIn")
    public  ApiResult<JwtRspRec> signIn(@RequestBody SignInRec sign){
        return ApiResult.success(signService.signIn(sign));
    }

    @Operation(summary = "注册")
    @PostMapping("signOn")
    public ApiResult<Void> signOn(@RequestBody SignOnRec sign){
        signService.signOn(sign);
        return ApiResult.success();
    }
}
