package com.luckykuang.auth.utils;

import com.luckykuang.auth.vo.PageVo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * 分页查询基础类
 * @author luckykuang
 * @date 2023/4/17 18:12
 */
public class PageUtils {

    private static final String UPDATE_TIME = "updateTime";

    private PageUtils(){}

    public static Pageable getPageable(PageVo page) {
        return PageRequest.of(
                page.getNumber(),
                page.getSize(),
                Sort.Direction.DESC,
                UPDATE_TIME);
    }
}
