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

package com.luckykuang.auth.base;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 基础类
 * @author luckykuang
 * @date 2023/4/17 16:40
 */
@Getter
@Setter
@ToString
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseParam implements Serializable {

    @Serial
    private static final long serialVersionUID = -2293166484510622651L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Schema(description = "主键id")
    private Long id;

    @CreatedBy
    @Column(nullable = false, updatable = false)
    @Schema(description = "创建人")
    private Long createBy;

    @LastModifiedBy
    @Column(nullable = false)
    @Schema(description = "更新人")
    private Long updateBy;

    @CreatedDate
    @Column(nullable = false, precision = 3, updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @LastModifiedDate
    @Column(nullable = false, precision = 3)
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
