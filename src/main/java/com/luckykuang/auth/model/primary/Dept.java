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

package com.luckykuang.auth.model.primary;

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
 * 部门表
 * @author luckykuang
 * @date 2023/5/16 16:26
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ky_dept")
@EntityListeners(AuditingEntityListener.class)
public class Dept extends BaseParam {
    @Serial
    private static final long serialVersionUID = 5541309154223194494L;
    @NotBlank
    @Column(nullable = false, length = 128)
    @Schema(description = "部门名称")
    private String name;

    @Schema(description = "部门父id")
    private Long parentId;

    @Column(length = 8)
    @Schema(description = "部门父id路径")
    private String treePath;

    @NotNull
    @Max(value = 99)
    @Column(nullable = false, length = 2)
    @Schema(description = "排序")
    private Integer sort;

    @NotNull
    @Column(nullable = false, length = 1)
    @Schema(description = "部门状态 1-启用 0-禁用",allowableValues = {"1","0"})
    private Integer status;
}
