package com.luckykuang.auth.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
public class Permissions extends BaseParams {

    @NotBlank
    @Column(nullable = false, length = 32)
    @Schema(title = "权限字段")
    private String permissionField;

    @NotBlank
    @Column(nullable = false, length = 128)
    @Schema(title = "权限名称")
    private String permissionName;

    @Schema(title = "描述")
    private String description;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            mappedBy = "permissionRoleFk"
    )
    @Schema(hidden = true)
    private List<RolePermission> rolePermissions = new ArrayList<>();
}
