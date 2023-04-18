package com.luckykuang.auth.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 用户角色表
 * @author luckykuang
 * @date 2023/4/11 13:18
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ky_auth_user_role")
public class UserRole {

    @EmbeddedId
    private UserRoleId id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @MapsId("userId")
    @JoinColumn(
            name = "user_id",
            foreignKey = @ForeignKey(
                    name = "ky_auth_user_role_fk"
            )
    )
    @Schema(hidden = true)
    private Users userRoleFk;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @MapsId("roleId")
    @JoinColumn(
            name = "role_id",
            foreignKey = @ForeignKey(
                    name = "ky_auth_role_user_fk"
            )
    )
    @Schema(hidden = true)
    private Roles roleUserFk;
}
