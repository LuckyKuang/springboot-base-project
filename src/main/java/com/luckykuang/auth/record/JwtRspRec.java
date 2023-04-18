package com.luckykuang.auth.record;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author luckykuang
 * @date 2023/4/18 16:48
 */
public record JwtRspRec(
        @Schema(title = "token") String accessToken,
        @Schema(title = "类型") String tokenType) {
}
