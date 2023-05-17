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

package com.luckykuang.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * @author luckykuang
 * @date 2023/4/21 22:45
 */
public record RoleReq(@NotBlank
                      @Pattern(regexp = "^ROLE_(.*)", message = "必须以<ROLE_>字符开头")
                      @Schema(description = "角色编号")
                      String code,
                      @NotBlank
                      @Schema(description = "角色名称")
                      String name,
                      @NotNull
                      @Max(value = 99)
                      @Schema(description = "排序")
                      Integer sort,
                      @NotNull
                      @Schema(description = "角色状态 1-启用 0-禁用",allowableValues = {"1","0"})
                      Integer status,
                      @NotBlank
                      @Schema(description = "描述")
                      String description,
                      @NotNull
                      @Schema(description = "权限id")
                      Long permissionId) {
    public static RoleReq from(RoleReq roleReq){
        return new RoleReq(
                roleReq.code.toUpperCase(),
                roleReq.name,
                roleReq.sort,
                roleReq.status,
                roleReq.description,
                roleReq.permissionId);
    }
}
