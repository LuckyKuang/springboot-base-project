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

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 分页参数
 * @author luckykuang
 * @date 2023/4/21 11:44
 */
@Getter
@Setter
public class PageVo {
    /**
     * 当前页(Jpa是0开头，Mybatis-Plus是1开头)
     */
    @Schema(description = "当前页",example = "0")
    private Integer current = 0;
    /**
     * 页大小
     */
    @Schema(description = "页大小",example = "10")
    private Integer size = 10;
}
