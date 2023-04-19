package com.luckykuang.auth.config;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 租户id 解析器
 * @author luckykuang
 * @date 2023/4/12 17:52
 */
@Component
public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver, HibernatePropertiesCustomizer {

    private static String DEFAULT_TENANT = "tenant";// unknown
    private static final String ADMIN_TENANT = "admin";

    public static void setTenant(String tenant){
        DEFAULT_TENANT = tenant;
    }

    @Override
    public String resolveCurrentTenantIdentifier() {
        String tenantId = RequestContext.getTenantId().orElse(DEFAULT_TENANT);
        // TODO 后期存入缓存
//        List<String> roleFields = new ArrayList<>(Arrays.asList("admin","tenant","operator","employee"));
//        if (!roleFields.isEmpty() && !roleFields.contains(tenantId)){
//            tenantId = defaultTenant;
//        }
        return ADMIN_TENANT.equals(tenantId) ? DEFAULT_TENANT : tenantId;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return false;
    }

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, this);
    }
}
