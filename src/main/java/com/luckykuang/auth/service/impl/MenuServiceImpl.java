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

package com.luckykuang.auth.service.impl;

import com.luckykuang.auth.model.Menu;
import com.luckykuang.auth.repository.MenuRepository;
import com.luckykuang.auth.request.MenuReq;
import com.luckykuang.auth.service.MenuService;
import com.luckykuang.auth.vo.PageResultVo;
import com.luckykuang.auth.vo.PageVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author luckykuang
 * @date 2023/4/20 14:18
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private MenuRepository menuRepository;

    @Override
    public Menu addMenu(MenuReq menuReq) {
        return null;
    }

    @Override
    public Menu getMenuById(Long id) {
        return null;
    }

    @Override
    public List<Menu> getMenus() {
        return null;
    }

    @Override
    public PageResultVo<Menu> getMenusByPage(PageVo page) {
        return null;
    }

    @Override
    public Menu updateMenu(Long id, MenuReq menuReq) {
        return null;
    }

    @Override
    public void delMenu(Long id) {
        // TODO document why this method is empty
    }
}
