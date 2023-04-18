package com.luckykuang.auth.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI 3 配置
 * @author luckykuang
 * @date 2023/4/18 15:04
 */
@Configuration
public class OpenApiConfig {

    /**
     * 指定某个路径 可多个指定
     */
    @Bean
    public GroupedOpenApi authApi() {
        return GroupedOpenApi.builder()
                .group("权限模块")
                .pathsToMatch("/auth/**")
                .build();
    }

    /**
     * License header
     */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(components())
                .info(new Info().title("Spring Boot3 Restful API")
                        .description("权限模块")
                        .version("v1.0.0")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("SpringDoc Wiki Documentation")
                        .url("https://springdoc.org/v2"));
    }

    /**
     * 支持jwt
     */
    private Components components(){
        return new Components().addSecuritySchemes("bearer-key",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT"));
    }
}
