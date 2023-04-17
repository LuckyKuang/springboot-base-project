package com.luckykuang.auth.service;

import com.luckykuang.auth.model.Users;
import com.luckykuang.auth.vo.PageVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

/**
 * @author luckykuang
 * @date 2023/4/11 16:36
 */
public interface UserService {

    Page<Users> queryUsersByPage(PageVo page);

    List<Users> queryUsers();

    Optional<Users> queryUserById(Long userId);

    Users insertUser(Users users);

    Users updateUser(Users users);

    void deleteUser(Long userId);
}
