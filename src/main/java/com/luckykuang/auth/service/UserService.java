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

package com.luckykuang.auth.service;

import com.luckykuang.auth.base.ApiResult;
import com.luckykuang.auth.model.User;
import com.luckykuang.auth.request.PasswordRed;
import com.luckykuang.auth.request.UserReq;
import com.luckykuang.auth.vo.PageResultVo;
import com.luckykuang.auth.vo.PageVo;
import com.luckykuang.auth.vo.UserDetailsVo;

import java.util.List;
import java.util.Optional;

/**
 * @author luckykuang
 * @date 2023/4/20 14:19
 */
public interface UserService {
    User addUser(UserReq userReq);

    User getUserById(Long id);

    Optional<User> getUserByUsername(String username);

    List<User> getUsers();

    PageResultVo<User> getUserByPage(PageVo page);

    User updateUser(Long id, UserReq userReq);

    ApiResult<Void> delUser(Long id);

    UserDetailsVo getUserDetails(String username);

    ApiResult<Void> updatePass(PasswordRed passwordRed);

    ApiResult<Void> resetPass();

    UserDetailsVo getUserInfo();
}
