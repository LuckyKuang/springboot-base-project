package com.luckykuang.auth.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 角色权限表
 * @author luckykuang
 * @date 2023/4/11 13:19
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ky_auth_role_permission")
public class RolePermission {

    @EmbeddedId
    private RolePermissionId id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @MapsId("roleId")
    @JoinColumn(
            name = "role_id",
            foreignKey = @ForeignKey(
                    name = "ky_auth_role_permission_fk"
            )
    )
    @Schema(hidden = true)
    private Roles rolePermissionFk;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @MapsId("permissionId")
    @JoinColumn(
            name = "permission_id",
            foreignKey = @ForeignKey(
                    name = "ky_auth_permission_role_fk"
            )
    )
    @Schema(hidden = true)
    private Permissions permissionRoleFk;
}
