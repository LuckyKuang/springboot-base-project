//package com.luckykuang.learning_demo.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
///**
// * 全局MVC拦截器
// * @author luckykuang
// * @date 2023/4/12 17:24
// */
//@Configuration
//public class GlobalWebMvcConfigurer implements WebMvcConfigurer {
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new GlobalInterceptor())
//                .addPathPatterns("/**")
//                .excludePathPatterns(
//                        "/auth/v1/login",
//                        "/auth/v1/logout",
//                        "/auth/v1/register"
//                );
//    }
//}
