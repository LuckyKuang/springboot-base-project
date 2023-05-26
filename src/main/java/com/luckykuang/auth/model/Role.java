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

package com.luckykuang.auth.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.luckykuang.auth.base.BaseParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serial;
import java.util.HashSet;
import java.util.Set;


/**
 * 角色表
 * @author luckykuang
 * @date 2023/4/11 13:18
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ky_role")
public class Role extends BaseParam {

    @Serial
    private static final long serialVersionUID = -6410618731179548963L;

    @NotBlank
    @Column(nullable = false, length = 128)
    @Schema(description = "角色编码")
    private String code;

    @NotBlank
    @Column(nullable = false, length = 128)
    @Schema(description = "角色名称")
    private String name;

    @NotNull
    @Max(value = 99)
    @Column(nullable = false, length = 2)
    @Schema(description = "排序")
    private Integer sort;

    @NotNull
    @Column(nullable = false, length = 1)
    @Schema(description = "角色状态 1-启用 0-禁用",allowableValues = {"1","0"})
    private Integer status;

    @Schema(description = "描述")
    private String description;

    @NotNull
    @Column(nullable = false, length = 1)
    @Schema(description = "数据权限 0-全部数据 1-本部门及子部门数据 2-本部门数据 3-本人数据")
    private Integer dataScope;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Menu> menus = new HashSet<>();
}
