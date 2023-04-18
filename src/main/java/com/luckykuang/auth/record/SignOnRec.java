package com.luckykuang.auth.record;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * @author luckykuang
 * @date 2023/4/18 17:13
 */
public record SignOnRec(
        @NotBlank @Schema(title = "名称") String name,
        @NotBlank @Schema(title = "用户名") String username,
        @NotBlank @Schema(title = "手机号") String phone,
        @NotBlank @Schema(title = "邮箱") String email,
        @NotBlank @Schema(title = "密码") String password) {
}
