package com.luckykuang.auth.service.impl;

import com.luckykuang.auth.constant.ErrorCodeEnum;
import com.luckykuang.auth.exception.BusinessException;
import com.luckykuang.auth.model.Roles;
import com.luckykuang.auth.model.UserRole;
import com.luckykuang.auth.model.UserRoleId;
import com.luckykuang.auth.model.Users;
import com.luckykuang.auth.repository.RoleRepository;
import com.luckykuang.auth.repository.UserRepository;
import com.luckykuang.auth.repository.UserRoleRepository;
import com.luckykuang.auth.service.RoleService;
import com.luckykuang.auth.utils.AssertUtils;
import com.luckykuang.auth.utils.PageUtils;
import com.luckykuang.auth.vo.PageVo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author luckykuang
 * @date 2023/4/11 16:37
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    public Page<Roles> queryRolesByPage(PageVo page) {
        Pageable pageable = PageUtils.getPageable(page);
        return roleRepository.findAll(pageable);
    }

    @Override
    public List<Roles> queryRoles() {
        return roleRepository.findAll(Sort.by("updateTime"));
    }

    @Override
    public Optional<Roles> queryRoleById(Long roleId) {
        return roleRepository.findById(roleId);
    }

    @Override
    public Roles insertRole(Roles roles) {
        Optional<Roles> rolesOptional = roleRepository.findByRoleField(roles.getRoleField());
        AssertUtils.isTrue(rolesOptional.isEmpty(), ErrorCodeEnum.FIELD_EXIST);

        return roleRepository.save(roles);
    }

    @Override
    public Roles updateRole(Roles roles) {
        Optional<Roles> repositoryById = roleRepository.findById(roles.getId());
        AssertUtils.isTrue(repositoryById.isPresent(), ErrorCodeEnum.ID_NOT_EXIST);
        Optional<Roles> rolesOptional = roleRepository.findByIdIsNotAndRoleName(roles.getId(), roles.getRoleName());
        AssertUtils.isTrue(rolesOptional.isEmpty(), ErrorCodeEnum.NAME_EXIST);

        return roleRepository.save(roles);
    }

    @Override
    public void deleteRole(Long roleId) {
        roleRepository.deleteById(roleId);
    }

    @Override
    public UserRole authUserRole(UserRoleId userRoleId) {
        Users users = userRepository.findById(userRoleId.getUserId())
                .orElseThrow(() -> new BusinessException(ErrorCodeEnum.BAD_REQUEST));
        Roles roles = roleRepository.findById(userRoleId.getRoleId())
                .orElseThrow(() -> new BusinessException(ErrorCodeEnum.BAD_REQUEST));

        UserRole userRole = new UserRole();
        userRole.setId(userRoleId);
        userRole.setUserRoleFk(users);
        userRole.setRoleUserFk(roles);
        return userRoleRepository.save(userRole);
    }
}
