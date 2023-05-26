///*
// * Copyright 2015-2023 the original author or authors.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      https://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.luckykuang.auth.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//
///**
// * 全局MVC拦截器
// * @author luckykuang
// * @date 2023/4/20 17:37
// */
//@Configuration
//public class WebMvcConfig implements WebMvcConfigurer {
//
//    private static final List<String> EXCLUDE_PATH_PATTERNS = new ArrayList<>(Arrays.asList(
//            "/auth/v1/sign/**",
//            "/v3/api-docs/**",
//            "/swagger-ui/**",
//            "/swagger-resources/**",
//            "/webjars/**",
//            "/doc.html",
//            "/favicon.ico"
//    ));
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        WebMvcConfigurer.super.addInterceptors(registry);
//        registry.addInterceptor(new WebMvcHandlerInterceptor())
//                .addPathPatterns("/**")
//                // 白名单
//                .excludePathPatterns(EXCLUDE_PATH_PATTERNS);
//    }
//}
