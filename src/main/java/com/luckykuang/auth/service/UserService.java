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
import com.luckykuang.auth.request.LoginReq;
import com.luckykuang.auth.request.RefreshReq;
import com.luckykuang.auth.request.UserReq;
import com.luckykuang.auth.response.CaptchaRsp;
import com.luckykuang.auth.response.TokenRsp;
import com.luckykuang.auth.vo.PageResultVo;
import com.luckykuang.auth.vo.PageVo;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * @author luckykuang
 * @date 2023/4/20 14:19
 */
public interface UserService {
    User addUser(UserReq userReq);

    User getUserById(Long id);

    User getUserByUsername(String username);

    List<User> getUsers();

    ApiResult<TokenRsp> login(LoginReq loginReq);

    PageResultVo<User> getUserByPage(PageVo page);

    User updateUser(Long id, UserReq userReq);

    void delUser(Long id);

    ApiResult<TokenRsp> refresh(RefreshReq refreshReq);

    CaptchaRsp getCaptcha();

    ApiResult<Void> logout(HttpServletRequest request);
}
