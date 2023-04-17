package com.luckykuang.auth.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色表
 * @author luckykuang
 * @date 2023/4/11 13:18
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ky_auth_role")
@EntityListeners(AuditingEntityListener.class)
public class Roles extends BaseParams {

    @Column(nullable = false)
    private Long parentId;

    @Column(length = 16)
    private String treeKey;

    @NotNull
    @Column(nullable = false)
    private Integer treeLevel;

    @NotBlank
    @Column(nullable = false, length = 32)
    private String roleField;

    @NotBlank
    @Column(nullable = false, length = 128)
    private String roleName;

    private String description;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            mappedBy = "roleUserFk"
    )
    private List<UserRole> userRoles = new ArrayList<>();

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            mappedBy = "rolePermissionFk"
    )
    private List<RolePermission> rolePermissions = new ArrayList<>();
}
