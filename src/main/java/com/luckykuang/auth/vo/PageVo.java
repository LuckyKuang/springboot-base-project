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
import lombok.Setter;

import java.util.Optional;

/**
 * 分页参数
 * @author luckykuang
 * @date 2023/4/21 11:44
 */
@Setter
public class PageVo {
    /**
     * 当前页
     */
    @Schema(description = "当前页",type = "Integer",defaultValue = "0")
    private Integer current;
    /**
     * 页大小
     */
    @Schema(description = "页大小",type = "Integer",defaultValue = "10")
    private Integer size;

    public Integer getCurrent() {
        Integer currentNumber = Optional.ofNullable(current).orElse(0);
        return currentNumber < 0 ? 0 : currentNumber;
    }

    public Integer getSize() {
        Integer sizeNumber = Optional.ofNullable(size).orElse(10);
        return sizeNumber > 0 ? sizeNumber : 1;
    }
}
