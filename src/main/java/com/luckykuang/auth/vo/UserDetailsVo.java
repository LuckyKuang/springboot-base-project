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

package com.luckykuang.auth.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.luckykuang.auth.model.Menu;
import com.luckykuang.auth.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * @author luckykuang
 * @date 2023/5/22 14:34
 */
@Getter
@Setter
public class UserDetailsVo {
    /**
     * 用户id
     */
    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "用户名")
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Schema(description = "部门id")
    private Long deptId;

    @Schema(description = "数据权限 0-全部数据 1-本部门及子部门数据 2-本部门数据 3-本人数据")
    private Integer dataScope;

    @Schema(description = "角色")
    private Set<Role> roles;

    @Schema(description = "菜单权限")
    private Set<Menu> menus;
}
