package com.luckykuang.auth.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.TenantId;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

/**
 * 基础类
 * @author luckykuang
 * @date 2023/4/17 16:40
 */
@Getter
@Setter
@MappedSuperclass
public class BaseParams {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Schema(title = "主键id")
    private Long id;

    @Column(nullable = false, length = 32)
    @TenantId
    @Schema(title = "租户id")
    private String tenantId;

    @CreatedBy
    @Column(nullable = false, updatable = false)
    @Schema(title = "创建人")
    private Long createBy;

    @LastModifiedBy
    @Column(nullable = false)
    @Schema(title = "更新人")
    private Long updateBy;

    @CreatedDate
    @Column(nullable = false, precision = 3, updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema( title = "创建时间")
    private LocalDateTime createTime;

    @LastModifiedDate
    @Column(nullable = false, precision = 3)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(title = "更新时间")
    private LocalDateTime updateTime;
}
