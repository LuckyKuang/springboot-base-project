package com.luckykuang.auth.record;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * @author luckykuang
 * @date 2023/4/18 16:53
 */
public record SignInRec(
        @NotBlank @Schema(title = "用户名") String username,
        @NotBlank @Schema(title = "密码") String password) {
}
