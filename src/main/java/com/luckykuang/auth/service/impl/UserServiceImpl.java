package com.luckykuang.auth.service.impl;

import com.luckykuang.auth.constant.ErrorCodeEnum;
import com.luckykuang.auth.model.Users;
import com.luckykuang.auth.repository.UserRepository;
import com.luckykuang.auth.service.UserService;
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
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Page<Users> queryUsersByPage(PageVo page) {
        Pageable pageable = PageUtils.getPageable(page);
        return userRepository.findAll(pageable);
    }

    @Override
    public List<Users> queryUsers() {
        return userRepository.findAll(Sort.by("updateTime"));
    }

    @Override
    public Optional<Users> queryUserById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Users insertUser(Users users) {
        Optional<Users> usersOptional = userRepository.findByUsername(users.getUsername());
        AssertUtils.isTrue(usersOptional.isEmpty(), ErrorCodeEnum.NAME_DUPLICATION);
        Optional<Users> byPhone = userRepository.findByPhone(users.getPhone());
        AssertUtils.isTrue(byPhone.isEmpty(),ErrorCodeEnum.PHONE_DUPLICATION);

        return userRepository.save(users);
    }

    @Override
    public Users updateUser(Users users) {
        Optional<Users> repositoryById = userRepository.findById(users.getId());
        AssertUtils.isTrue(repositoryById.isPresent(), ErrorCodeEnum.ID_NOT_EXIST);
        Optional<Users> usersOptional = userRepository.findByIdIsNotAndUsername(users.getId(), users.getUsername());
        AssertUtils.isTrue(usersOptional.isEmpty(), ErrorCodeEnum.NAME_DUPLICATION);

        return userRepository.save(users);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
