package com.luckykuang.learning_demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author luckykuang
 * @date 2023/4/14 15:50
 */
@Configuration
@EnableWebSecurity
//@RequiredArgsConstructor
public class GlobalSecurityConfig extends WebSecurityConfiguration {

//    private final PasswordEncoder passwordEncoder;
//    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(requests -> {
                    try {
                        requests
                                // 放行首页
                                .requestMatchers("/", "/index").permitAll()
                                .requestMatchers("/", "/register").permitAll()
                                .requestMatchers("/", "/login").permitAll()
                                // 放行资源目录
                                .requestMatchers("/static/**", "/resources/**").permitAll()
                                // 其他都要校验
                                .anyRequest().authenticated()
                                .and().csrf(AbstractHttpConfigurer::disable);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
        }).build();
    }


//    @Bean
//    public DaoAuthenticationProvider daoAuthenticationProvider(){
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setPasswordEncoder(passwordEncoder);
//        provider.setUserDetailsService(userDetailsService);
//        return provider;
//    }
}
