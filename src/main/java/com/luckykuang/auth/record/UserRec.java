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
import com.luckykuang.auth.validation.Mobile;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * @author luckykuang
 * @date 2023/4/18 17:13
 */
public record UserRec(
        @NotBlank
        @Schema(description = "用户名称")
        String name,
        @NotBlank
        @Schema(description = "用户名")
        String username,
        @NotBlank
        @Mobile
        @Schema(description = "手机号")
        String phone,
        @Email
        @Schema(description = "邮箱")
        String email,
        @NotNull
        @Schema(description = "性别 1-男 0-女",allowableValues = {"1","0"})
        Integer gender,
        @NotBlank
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @Schema(description = "密码")
        String password,
        @Schema(description = "用户头像")
        String avatar,
        @NotNull
        @Schema(description = "用户状态 1-启用 0-禁用",allowableValues = {"1", "0"})
        Integer status,
        @NotNull
        @Schema(description = "角色id")
        Long roleId,
        @NotNull
        @Schema(description = "部门id")
        Long deptId) {
    public static UserRec from(UserRec userRec){
        return new UserRec(
                userRec.name,
                userRec.username.toLowerCase(),
                userRec.phone,
                userRec.email,
                userRec.gender,
                userRec.password,
                userRec.avatar,
                userRec.status,
                userRec.roleId,
                userRec.deptId);
    }
}
