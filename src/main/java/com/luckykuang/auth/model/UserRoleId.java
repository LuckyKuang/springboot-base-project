package com.luckykuang.auth.model;

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
    private Long userId;

    @Column(name = "role_id")
    private Long roleId;
}
