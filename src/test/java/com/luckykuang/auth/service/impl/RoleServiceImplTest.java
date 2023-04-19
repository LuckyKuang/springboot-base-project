package com.luckykuang.auth.service.impl;

import com.luckykuang.auth.exception.BusinessException;
import com.luckykuang.auth.model.Roles;
import com.luckykuang.auth.repository.RoleRepository;
import com.luckykuang.auth.repository.UserRepository;
import com.luckykuang.auth.repository.UserRoleRepository;
import com.luckykuang.auth.utils.PageUtils;
import com.luckykuang.auth.vo.PageVo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 * @author luckykuang
 * @date 2023/4/19 14:47
 */
@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    private static final String TENANT = "tenant";
    @Mock private RoleRepository roleRepository;
    @Mock private UserRepository userRepository;
    @Mock private UserRoleRepository userRoleRepository;

    @InjectMocks
    private RoleServiceImpl service;

//    @BeforeEach
//    void setUp() {
//        service = new RoleServiceImpl(roleRepository,userRepository,userRoleRepository);
//    }

    @Test
    void queryRolesByPage() {
        PageVo pageVo = new PageVo();
        service.queryRolesByPage(pageVo);
        Pageable pageable = PageUtils.getPageable(pageVo);
        verify(roleRepository).findAll(pageable);
    }

    @Test
    void queryRoles() {
        service.queryRoles();
        verify(roleRepository).findAll(Sort.by("updateTime"));
    }

    @Test
    void queryRoleById() {
        long roleId = 1;

        service.queryRoleById(roleId);

        verify(roleRepository).findById(roleId);
    }

    @Test
    void insertRole() {
        Roles roles = getRoles();

        service.insertRole(roles);

        ArgumentCaptor<Roles> rolesArgumentCaptor = ArgumentCaptor.forClass(Roles.class);
        verify(roleRepository).save(rolesArgumentCaptor.capture());

        Roles captorValue = rolesArgumentCaptor.getValue();
        assertThat(captorValue).isEqualTo(roles);
    }

    @Test
    void updateRole() {
        Roles roles = getRoles();

        when(roleRepository.findById(roles.getId())).thenReturn(Optional.of(roles));
        when(roleRepository.findByIdIsNotAndRoleName(roles.getId(), roles.getRoleName())).thenReturn(Optional.empty());

        service.updateRole(roles);

        ArgumentCaptor<Roles> rolesArgumentCaptor = ArgumentCaptor.forClass(Roles.class);
        verify(roleRepository).save(rolesArgumentCaptor.capture());

        Roles captorValue = rolesArgumentCaptor.getValue();
        assertThat(captorValue).isEqualTo(roles);
    }

    @Test
    void deleteRole() {
        long roleId = 1;
        boolean existsById = roleRepository.existsById(roleId);
        given(existsById).willReturn(false);

        service.deleteRole(roleId);

        verify(roleRepository).deleteById(roleId);
    }

    @Test
    void deleteRoleCheckNotFound() {
        long roleId = 1;
        boolean existsById = roleRepository.existsById(roleId);
        given(existsById).willReturn(true);

        assertThatThrownBy(() -> service.deleteRole(roleId))
                .isInstanceOf(BusinessException.class);

        verify(roleRepository, never()).deleteById(any());
    }

    private Roles getRoles() {
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
        roles.setTenantId("tenant");
        roles.setCreateTime(LocalDateTime.now());
        roles.setUpdateTime(LocalDateTime.now());
        return roles;
    }
}