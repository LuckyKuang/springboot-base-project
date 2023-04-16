package com.luckykuang.auth.model;

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

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(
            name = "user_id",
            foreignKey = @ForeignKey(
                    name = "ky_auth_user_role_fk"
            )
    )
    private Users userRoleFk;

    @ManyToOne
    @MapsId("roleId")
    @JoinColumn(
            name = "role_id",
            foreignKey = @ForeignKey(
                    name = "ky_auth_role_user_fk"
            )
    )
    private Roles roleUserFk;
}
