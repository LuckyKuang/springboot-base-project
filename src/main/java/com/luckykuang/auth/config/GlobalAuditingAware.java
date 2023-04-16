package com.luckykuang.auth.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 全局设置userId
 * @author luckykuang
 * @date 2023/4/10 18:31
 */
@Component
public class GlobalAuditingAware implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        return RequestContext.getUserId();
    }
}
