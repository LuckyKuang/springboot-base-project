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
import jakarta.persistence.Column;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * @author luckykuang
 * @date 2023/4/21 23:00
 */
public record MenuReq(
        @NotBlank
        @Schema(description = "菜单名称")
        String name,
        @Schema(description = "菜单父id")
        Long parentId,
        @NotNull
        @Schema(description = "菜单类型 1-目录 2-菜单 3-按钮 4-外链",allowableValues = {"1","2","3","4"})
        Integer type,
        @Column(length = 128)
        @Schema(description = "路由路径(浏览器地址栏路径)")
        String routerPath,
        @Column(length = 128)
        @Schema(description = "组件路径(vue页面完整路径，省略.vue后缀)")
        String componentPath,
        @Column(length = 128)
        @Schema(description = "权限标识")
        String permissionName,
        @NotNull
        @Schema(description = "显示状态 1-显示 0-隐藏",allowableValues = {"1","0"})
        Integer status,
        @NotNull
        @Max(value = 99)
        @Schema(description = "排序")
        Integer sort,
        @Column(length = 128)
        @Schema(description = "图标")
        String icon,
        @Schema(description = "外链地址")
        String outerLinkUrl,
        @Schema(description = "跳转地址")
        String redirectUrl) {
    public static MenuReq from(MenuReq menuReq){
        return new MenuReq(
                menuReq.name,
                menuReq.parentId,
                menuReq.type,
                menuReq.routerPath,
                menuReq.componentPath,
                menuReq.permissionName.toLowerCase(),
                menuReq.status,
                menuReq.sort,
                menuReq.icon,
                menuReq.outerLinkUrl,
                menuReq.redirectUrl);
    }
}
