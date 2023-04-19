package com.luckykuang.auth.repository;

import com.luckykuang.auth.constant.ErrorCodeEnum;
import com.luckykuang.auth.constant.UserStatusEnum;
import com.luckykuang.auth.exception.BusinessException;
import com.luckykuang.auth.model.Roles;
import com.luckykuang.auth.model.UserRole;
import com.luckykuang.auth.model.UserRoleId;
import com.luckykuang.auth.model.Users;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author luckykuang
 * @date 2023/4/18 00:50
 */
@DataJpaTest
class UserRoleRepositoryTest {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    @Autowired
    UserRoleRepositoryTest(UserRepository userRepository,
                           RoleRepository roleRepository,
                           UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @BeforeEach
    void initUserRole(){
        Roles roles = new Roles();
        roles.setId(1L);
        roles.setRoleField("xxx");
        roles.setRoleName("xxx");
        roles.setDescription("xxx");
        roles.setParentId(0L);
        roles.setTreeKey("xxx");
        roles.setTenantId("tenant");
        roles.setTreeLevel(1);
        roles.setCreateBy(1L);
        roles.setUpdateBy(1L);
        roles.setCreateTime(LocalDateTime.now());
        roles.setUpdateTime(LocalDateTime.now());
        roleRepository.save(roles);

        Users users = new Users();
        users.setName("xxx");
        users.setUsername("xxx");
        users.setPassword("xxx");
        users.setEmail("xxx@gmail.com");
        users.setPhone("13011112222");
        users.setUserStatus(UserStatusEnum.ACTIVE);
        users.setId(1L);
        users.setTenantId("tenant");
        users.setCreateBy(1L);
        users.setUpdateBy(1L);
        users.setCreateTime(LocalDateTime.now());
        users.setUpdateTime(LocalDateTime.now());
        userRepository.save(users);

        UserRole userRole = new UserRole();
        UserRoleId id = new UserRoleId();
        id.setUserId(1L);
        id.setRoleId(1L);
        userRole.setId(id);
        userRole.setRoleUserFk(roles);
        userRole.setUserRoleFk(users);
        userRoleRepository.save(userRole);
    }

    @AfterEach
    void destroyData(){
        userRepository.deleteAll();
        roleRepository.deleteAll();
        userRoleRepository.deleteAll();
    }

    @Test
    void findUserRoleByUserRoleFk() {
        Users users = userRepository.findById(1L)
                .orElseThrow(() -> new BusinessException(ErrorCodeEnum.BAD_REQUEST));;
        UserRole userRole = userRoleRepository.findUserRoleByUserRoleFk(users)
                .orElseThrow(() -> new BusinessException(ErrorCodeEnum.BAD_REQUEST));
        Long roleId = 1L;
        assertEquals(roleId, userRole.getId().getRoleId());
    }
}