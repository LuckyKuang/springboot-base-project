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

package com.luckykuang.auth.utils;

import com.luckykuang.auth.vo.PageResultVo;
import com.luckykuang.auth.vo.PageVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * 分页查询基础类
 * @author luckykuang
 * @date 2023/4/21 15:07
 */
public final class PageUtils {

    private PageUtils(){}

    /**
     * 获取分页信息
     * @param page 分页对象
     * @param sort 排序 Sort.Direction.ASC OR Sort.Direction.DESC
     * @param sortField 排序字段
     * @return
     */
    public static Pageable getPageable(PageVo page, Sort.Direction sort, String... sortField) {
        return PageRequest.of(
                page.getCurrent(),
                page.getSize(),
                sort,
                sortField);
    }

    /**
     * 分页数据封装
     * @param page 分页结果
     * @return 自定义封装数据
     * @param <T> 分页结果实体
     */
    public static <T> PageResultVo<T> getPageResult(Page<T> page) {
        PageResultVo<T> pageResultVo = new PageResultVo<>();
        pageResultVo.setTotalElements(page.getTotalElements());
        pageResultVo.setTotalPages(page.getTotalPages());
        pageResultVo.setCurrent(page.getNumber());
        pageResultVo.setSize(page.getSize());
        pageResultVo.setContent(page.getContent());
        return pageResultVo;
    }
}
