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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * redis 配置类
 * @author luckykuang
 * @date 2023/4/23 18:10
 */
@Configuration
public class RedisConfig {

    /**
     * 改变 redis 默认的序列化方式
     * 将 JdkSerializationRedisSerializer 设置为 GenericJackson2JsonRedisSerializer
     * @param factory
     * @return
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        // key采用String的序列化方式
        template.setKeySerializer(RedisSerializer.string());
        // value序列化方式采用jackson
        template.setValueSerializer(RedisSerializer.json());

        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(RedisSerializer.string());
        // hash的value序列化方式采用jackson
        template.setHashValueSerializer(RedisSerializer.json());

        template.afterPropertiesSet();
        return template;
    }
}
