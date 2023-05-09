/*
 * Copyright 2015-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.luckykuang.auth.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.luckykuang.auth.base.BaseParam;
import com.luckykuang.auth.enums.UserStatusEnum;
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

import java.io.Serial;

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
        name = "ky_user",
        uniqueConstraints ={
            @UniqueConstraint(name = "ky_user_phone_uk", columnNames = "phone")
        }
)
@EntityListeners(AuditingEntityListener.class)
public class User extends BaseParam {

    @Serial
    private static final long serialVersionUID = -3932246348124717588L;
    @NotBlank
    @Column(nullable = false, length = 128)
    @Schema(description = "名称")
    private String name;

    @NotBlank
    @Column(nullable = false, length = 128)
    @Schema(description = "用户名")
    private String username;

    @NotBlank
    @Column(nullable = false, length = 128)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "密码")
    private String password;

    @Email
    @Column(length = 128)
    @Schema(description = "邮箱")
    private String email;

    @NotBlank
    @Mobile
    @Column(nullable = false, length = 32)
    @Schema(description = "手机号码")
    private String phone;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    @Schema(description = "用户状态")
    private UserStatusEnum userStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    private Role role;

    @Override
    public String toString() {
        return "User{" + super.toString() +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", userStatus=" + userStatus +
                ", role=" + role +
                '}';
    }
}
