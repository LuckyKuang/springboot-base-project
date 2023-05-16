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

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * @author luckykuang
 * @date 2023/4/21 23:00
 */
public record PermissionRec(@NotBlank
                            @Schema(description = "权限编号")
                            String code,
                            @NotBlank
                            @Schema(description = "权限名称")
                            String name,
                            @NotBlank
                            @Schema(description = "描述")
                            String description) {
    public static PermissionRec from(PermissionRec permissionRec){
        return new PermissionRec(
                permissionRec.code.toLowerCase(),
                permissionRec.name,
                permissionRec.description);
    }
}
