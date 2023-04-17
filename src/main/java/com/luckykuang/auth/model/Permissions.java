package com.luckykuang.auth.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.TenantId;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 权限表
 * @author luckykuang
 * @date 2023/4/11 13:19
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ky_auth_permission")
@EntityListeners(AuditingEntityListener.class)
public class Permissions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id", nullable = false)
    private Long permissionId;

    @NotBlank
    @Column(nullable = false, length = 32)
    @TenantId
    private String tenantId;

    @Column(nullable = false)
    private Long parentId;

    @Column(length = 16)
    private String treeKey;

    @NotNull
    @Column(nullable = false)
    private Integer treeLevel;

    @NotBlank
    @Column(nullable = false, length = 32)
    private String permissionField;

    @NotBlank
    @Column(nullable = false, length = 128)
    private String permissionName;

    private String description;

    @CreatedBy
    @Column(nullable = false, updatable = false)
    private Long createBy;

    @LastModifiedBy
    @Column(nullable = false)
    private Long updateBy;

    @CreatedDate
    @Column(nullable = false, precision = 3, updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @LastModifiedDate
    @Column(nullable = false, precision = 3)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            mappedBy = "permissionRoleFk"
    )
    private List<RolePermission> rolePermissions = new ArrayList<>();
}
