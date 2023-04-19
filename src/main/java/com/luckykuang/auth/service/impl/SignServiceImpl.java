package com.luckykuang.auth.service.impl;

import com.luckykuang.auth.constant.ErrorCodeEnum;
import com.luckykuang.auth.constant.UserStatusEnum;
import com.luckykuang.auth.exception.BusinessException;
import com.luckykuang.auth.model.Roles;
import com.luckykuang.auth.model.UserRole;
import com.luckykuang.auth.model.UserRoleId;
import com.luckykuang.auth.model.Users;
import com.luckykuang.auth.record.JwtRspRec;
import com.luckykuang.auth.record.SignInRec;
import com.luckykuang.auth.record.SignOnRec;
import com.luckykuang.auth.repository.RoleRepository;
import com.luckykuang.auth.repository.UserRepository;
import com.luckykuang.auth.repository.UserRoleRepository;
import com.luckykuang.auth.security.JwtTokenProvider;
import com.luckykuang.auth.service.SignService;
import com.luckykuang.auth.utils.AssertUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author luckykuang
 * @date 2023/4/18 17:51
 */
@Service
@RequiredArgsConstructor
public class SignServiceImpl implements SignService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    public JwtRspRec signIn(SignInRec sign) {
        Authentication authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        sign.username(),
                        sign.password()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        // 从tokenProvider获取令牌
        String token = tokenProvider.generateToken(authenticate);
        return new JwtRspRec(token, "Bearer");
    }

    @Override
    public void signOn(SignOnRec sign) {
        AssertUtils.isTrue(!userRepository.existsByUsername(sign.username()), ErrorCodeEnum.USERNAME_EXIST);
        AssertUtils.isTrue(!userRepository.existsByEmail(sign.email()), ErrorCodeEnum.EMAIL_EXIST);
        AssertUtils.isTrue(!userRepository.existsByPhone(sign.phone()), ErrorCodeEnum.PHONE_EXIST);

        Users users = new Users();
        users.setName(sign.name());
        users.setUsername(sign.username());
        users.setPassword(passwordEncoder.encode(sign.password()));
        users.setEmail(sign.email());
        users.setPhone(sign.phone());
        users.setUserStatus(UserStatusEnum.ACTIVE);
        users.setCreateBy(1L);
        users.setUpdateBy(1L);
        Users user = userRepository.save(users);

        Roles role = roleRepository.findRolesByRoleField(user.getTenantId())
                .orElseThrow(() -> new BusinessException(ErrorCodeEnum.INTERNAL_SERVER_ERROR));
        UserRoleId userRoleId = new UserRoleId();
        userRoleId.setRoleId(role.getId());
        userRoleId.setUserId(user.getId());
        UserRole userRole = new UserRole();
        userRole.setId(userRoleId);
        userRole.setUserRoleFk(user);
        userRole.setRoleUserFk(role);
        userRoleRepository.save(userRole);
    }
}
