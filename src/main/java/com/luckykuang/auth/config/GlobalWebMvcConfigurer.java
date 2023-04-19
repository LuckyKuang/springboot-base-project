package com.luckykuang.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 全局MVC拦截器
 * @author luckykuang
 * @date 2023/4/12 17:24
 */
@Configuration
public class GlobalWebMvcConfigurer implements WebMvcConfigurer {

    private static final List<String> EXCLUDE_PATH_PATTERNS = new ArrayList<>(Arrays.asList(
            "/v3/api-docs/**",
            "/swagger-ui/**"
    ));

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new GlobalInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(EXCLUDE_PATH_PATTERNS);
    }
}
