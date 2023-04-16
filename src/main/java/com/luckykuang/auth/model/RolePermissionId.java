package com.luckykuang.auth.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
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

    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "permission_id")
    private Long permissionId;
}
