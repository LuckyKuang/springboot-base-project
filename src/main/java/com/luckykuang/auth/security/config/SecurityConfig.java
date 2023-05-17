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

package com.luckykuang.auth.security.config;

import com.luckykuang.auth.security.exception.JwtAccessDeniedHandler;
import com.luckykuang.auth.security.exception.JwtAuthenticationEntryPoint;
import com.luckykuang.auth.security.filter.CaptchaFilter;
import com.luckykuang.auth.security.filter.JwtAuthenticationFilter;
import com.luckykuang.auth.security.userdetails.LoginUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.Duration;
import java.util.List;

import static java.util.Collections.singletonList;

/**
 * Spring Security 配置类
 * @author luckykuang
 * @date 2023/4/20 16:08
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("#{'${app.cors-urls}'.split(',')}")
    private List<String> corsUrls;
    @Value("#{'${app.cors-methods}'.split(',')}")
    private List<String> corsMethods;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final LoginUserDetailsService userDetailsService;
//    private final LogoutHandler logoutHandler;


    /**
     * 安全校验
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 禁用csrf，防止跨站伪造
                .csrf().disable()
                // 跨域配置
//                .cors(cors-> cors
//                        // 自定义跨域配置
//                        .configurationSource(corsConfigurationSource()))
                // jwt是无状态的，不需要session
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 过滤配置
                .authorizeHttpRequests(authorize -> authorize
                        // 登录和刷新令牌放行
                        .requestMatchers("/auth/v1/sign/login").permitAll()
                        .requestMatchers("/auth/v1/sign/refresh").permitAll()
                        .anyRequest().authenticated()
                )
                // 验证码过滤器校验
//                .addFilterBefore(new CaptchaFilter(),UsernamePasswordAuthenticationFilter.class)
                // jwt认证
//                .authenticationProvider(authenticationProvider())
                // jwt过滤器校验
//                .addFilterBefore(jwtAuthenticationFilter,UsernamePasswordAuthenticationFilter.class)
                // 异常处理
                .exceptionHandling(exceptions -> exceptions
                        // 未登录自定义处理
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                        // 没权限自定义处理
                        .accessDeniedHandler(new JwtAccessDeniedHandler()));
        http.addFilterBefore(new CaptchaFilter(),UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter,UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * Security 忽略资源路径
     */
    @Bean
    public WebSecurityCustomizer ignoringCustomizer() {
        return web -> web.ignoring()
                .requestMatchers(
                        "/auth/v1/sign/getCaptcha",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-resources/**",
                        "/webjars/**",
                        "/doc.html");
    }

    /**
     * 加密工具
     */
    @Bean public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 身份验证
     */
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    /**
     * 身份验证管理器
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * Security 自定义跨域配置
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true); // 用户凭证
        configuration.setAllowedOrigins(corsUrls); // 请求url
        configuration.setAllowedMethods(corsMethods); // 请求方法
        configuration.setAllowedHeaders(singletonList("*")); // 请求头
        configuration.setMaxAge(Duration.ofHours(1)); // 客户端缓存时间
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
