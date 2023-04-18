package com.luckykuang.auth.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.luckykuang.auth.constant.UserStatusEnum;
import com.luckykuang.auth.validation.Mobile;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
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
 * 用户表
 * @author luckykuang
 * @date 2023/4/11 13:17
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "ky_auth_user",
        uniqueConstraints ={
            @UniqueConstraint(name = "ky_auth_user_phone_uk", columnNames = "phone")
        }
)
@EntityListeners(AuditingEntityListener.class)
public class Users extends BaseParams {

    @NotBlank
    @Column(nullable = false, length = 128)
    @Schema(title = "名称")
    private String name;

    @NotBlank
    @Column(nullable = false, length = 128)
    @Schema(title = "用户名")
    private String username;

    @NotBlank
    @Column(nullable = false, length = 128)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(title = "密码")
    private String password;

    @Email
    @Column(length = 128)
    @Schema(title = "邮箱")
    private String email;

    @NotBlank
    @Mobile
    @Column(nullable = false, length = 32)
    @Schema(title = "手机号码")
    private String phone;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    @Schema(title = "用户状态")
    private UserStatusEnum userStatus;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            mappedBy = "userRoleFk"
    )
    @Schema(hidden = true)
    private List<UserRole> userRoles = new ArrayList<>();
}
