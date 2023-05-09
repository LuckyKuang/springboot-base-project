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

package com.luckykuang.auth.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * OpenAPI 3 配置
 * @author luckykuang
 * @date 2023/4/18 15:04
 */
@Configuration
public class SwaggerConfig {

    /**
     * GroupedOpenApi 是对接口文档分组，等同于 swagger 的 Docket
     */
    @Bean
    public GroupedOpenApi authApi() {
        return GroupedOpenApi.builder()
                .group("权限模块")
                .pathsToMatch("/auth/**")
                .build();
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                // 支持认证
                .components(new Components()
                        .addSecuritySchemes(
                                AUTHORIZATION,
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                // 全局安全校验项，也可以在对应的controller上的每个方法加注解 @SecurityRequirement(name = "Authorization")
                // knife4j-4.1.0版本有bug，目前无法全局设置，需要手动每个方法上添加，等后续优化吧
                .addSecurityItem(new SecurityRequirement().addList(AUTHORIZATION))
                .info(new Info()
                        .title("Spring Boot Base Project")
                        .description("Authorization module")
                        .version("v1.0.0")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://github.com/LuckyKuang/spring-boot-base-project/blob/master/LICENSE"))
                        .contact(new Contact()
                                .name("luckykuang")
                                .email("luckykuang@foxmail.com")
                                .url("https://github.com/luckykuang")))
                .externalDocs(new ExternalDocumentation()
                        .description("SpringDoc Wiki Documentation")
                        .url("https://springdoc.org/v2"));
    }
}
