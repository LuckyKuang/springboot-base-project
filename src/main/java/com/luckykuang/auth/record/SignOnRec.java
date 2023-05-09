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

package com.luckykuang.auth.record;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.luckykuang.auth.enums.UserStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * @author luckykuang
 * @date 2023/4/18 17:13
 */
public record SignOnRec(
        @NotBlank @Schema(description = "名称") String name,
        @NotBlank @Schema(description = "用户名") String username,
        @NotBlank @Schema(description = "手机号") String phone,
        @NotBlank @Schema(description = "邮箱") String email,
        @NotBlank @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) @Schema(description = "密码") String password,
        @NotNull @Schema(description = "用户状态") @Enumerated(EnumType.STRING) UserStatusEnum userStatus,
        @NotNull @Schema(description = "角色id") Long roleId) {
    public static SignOnRec from(SignOnRec signOnRec){
        return new SignOnRec(signOnRec.name,signOnRec.username.toLowerCase(),signOnRec.phone,signOnRec.email,
                signOnRec.password,signOnRec.userStatus,signOnRec.roleId);
    }
}
