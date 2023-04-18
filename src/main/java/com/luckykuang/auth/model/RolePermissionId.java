package com.luckykuang.auth.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * 角色权限联合主键
 * @author luckykuang
 * @date 2023/4/12 11:48
 */
@Getter
@Setter
@Embeddable
public class RolePermissionId implements Serializable {

    @Serial
    private static final long serialVersionUID = 1086421163657268730L;

    @NotNull
    @Column(name = "role_id")
    @Schema(title = "角色id")
    private Long roleId;

    @NotNull
    @Column(name = "permission_id")
    @Schema(title = "权限id")
    private Long permissionId;
}
