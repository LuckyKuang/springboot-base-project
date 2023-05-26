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

import com.luckykuang.auth.base.BaseParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;

/**
 * 菜单权限表
 * @author luckykuang
 * @date 2023/5/16 17:20
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ky_menu")
@EntityListeners(AuditingEntityListener.class)
public class Menu extends BaseParam {
    @Serial
    private static final long serialVersionUID = 4319502274280513148L;
    @NotBlank
    @Column(nullable = false, length = 128)
    @Schema(description = "菜单名称")
    private String name;

    @Schema(description = "菜单父id")
    private Long parentId;

    @NotNull
    @Column(nullable = false, length = 1)
    @Schema(description = "菜单类型 1-目录 2-菜单 3-按钮 4-外链",allowableValues = {"1","2","3","4"})
    private Integer type;

    @Column(length = 128)
    @Schema(description = "路由路径(浏览器地址栏路径)")
    private String routerPath;

    @Column(length = 128)
    @Schema(description = "组件路径(vue页面完整路径，省略.vue后缀)")
    private String componentPath;


    @Column(length = 128)
    @Schema(description = "权限标识")
    private String permissionName;

    @NotNull
    @Column(nullable = false, length = 1)
    @Schema(description = "显示状态 1-显示 0-隐藏",allowableValues = {"1","0"})
    private Integer status;

    @NotNull
    @Max(value = 99)
    @Column(nullable = false, length = 2)
    @Schema(description = "排序")
    private Integer sort;

    @Column(length = 128)
    @Schema(description = "图标")
    private String icon;

    @Schema(description = "外链地址")
    private String outerLinkUrl;

    @Schema(description = "跳转地址")
    private String redirectUrl;
}
