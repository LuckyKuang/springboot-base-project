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

import java.util.List;

/**
 * @author luckykuang
 * @date 2023/5/6 14:25
 */
@Getter
@Setter
public class PageResultVo<T> extends PageVo{
    /**
     * 总分页数
     */
    @Schema(description = "总分页数")
    private Integer totalPages;

    /**
     * 总记录数
     */
    @Schema(description = "总记录数")
    private long totalElements;

    /**
     * 分页数据
     */
    @Schema(description = "分页数据")
    private List<T> content;
}
