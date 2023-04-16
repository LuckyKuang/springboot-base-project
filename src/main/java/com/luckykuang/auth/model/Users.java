package com.luckykuang.auth.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.luckykuang.auth.constant.UserStatusEnum;
import com.luckykuang.auth.validation.Mobile;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
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
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotBlank
    @Column(nullable = false, length = 32)
    @TenantId
    private String tenantId;

    @NotBlank
    @Column(nullable = false, length = 128)
    private String username;

    @NotBlank
    @Column(nullable = false, length = 128)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String passwd;

    @Email
    @Column(length = 128)
    private String email;

    @NotBlank
    @Mobile
    @Column(nullable = false, length = 32)
    private String phone;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private UserStatusEnum userStatus;

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

    @OneToMany(
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            mappedBy = "userRoleFk"
    )
    private List<UserRole> userRoles = new ArrayList<>();
}
