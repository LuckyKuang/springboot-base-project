package com.luckykuang.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * @author luckykuang
 * @date 2023/5/16 21:59
 */
public record RefreshReq(@NotBlank
                         @Schema(description = "刷新令牌")
                         String refreshToken) {
}
