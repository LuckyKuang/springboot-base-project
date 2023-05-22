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

package com.luckykuang.auth.security.userdetails;

import com.luckykuang.auth.service.UserService;
import com.luckykuang.auth.vo.UserDetailsVo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author luckykuang
 * @date 2023/4/20 17:57
 */
@Service
@RequiredArgsConstructor
public class LoginUserDetailsService implements UserDetailsService {

    private final UserService userService;

    /**
     * LoginUserDetails 继承于 UserDetails，故返回值可以是LoginUserDetails
     * @param username 用户名
     * @return UserDetails
     */
    @Override
    public LoginUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetailsVo user = userService.getUserDetails(username);
        return new LoginUserDetails(
                user.getUserId(),
                user.getUsername(),
                user.getPassword(),
                user.getDeptId(),
                user.getDataScope(),
                user.getRoles(),
                user.getMenus()
        );
    }
}
