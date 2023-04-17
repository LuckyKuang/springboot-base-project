package com.luckykuang.auth.vo;

import java.util.Optional;

/**
 * 分页参数
 * @author luckykuang
 * @date 2023/4/17 17:42
 */
public class PageVo {

    /**
     * 当前页
     */
    private Integer number;
    /**
     * 页大小
     */
    private Integer size;

    public Integer getNumber() {
        return Optional.ofNullable(number).orElse(0);
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getSize() {
        return Optional.ofNullable(size).orElse(1);
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
