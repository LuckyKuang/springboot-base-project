package com.luckykuang.auth.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户角色联合主键
 * @author luckykuang
 * @date 2023/4/12 11:39
 */
@Getter
@Setter
@Embeddable
public class UserRoleId implements Serializable {

    @Serial
    private static final long serialVersionUID = 2989414605960746280L;

    @Column(name = "user_id")
    @Schema(title = "用户id")
    private Long userId;

    @Column(name = "role_id")
    @Schema(title = "角色id")
    private Long roleId;
}
