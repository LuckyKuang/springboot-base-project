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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.luckykuang.auth.utils.DateUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Jackson 配置类
 * @author luckykuang
 * @date 2023/5/10 16:56
 */
@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();

        JavaTimeModule javaTimeModule = new JavaTimeModule();
        // Long/BigInteger/BigDecimal类型转String类型，解决精度丢失问题，特别是前端js精度丢失问题
        javaTimeModule.addSerializer(Long.class, ToStringSerializer.instance);
        javaTimeModule.addSerializer(Long.TYPE,ToStringSerializer.instance);
        javaTimeModule.addSerializer(BigInteger.class, ToStringSerializer.instance);
        javaTimeModule.addSerializer(BigDecimal.class, ToStringSerializer.instance);

        // String转LocalDateTime
        // 序列化
        javaTimeModule.addSerializer(LocalDateTime.class,
                new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DateUtils.YYYY_MM_DD_HH_MM_SS)));
        javaTimeModule.addSerializer(LocalDate.class,
                new LocalDateSerializer(DateTimeFormatter.ofPattern(DateUtils.YYYY_MM_DD)));
        javaTimeModule.addSerializer(LocalTime.class,
                new LocalTimeSerializer(DateTimeFormatter.ofPattern(DateUtils.HH_MM_SS)));

        // 反序列，只能反序列化Body里的json格式数据
        javaTimeModule.addDeserializer(LocalDateTime.class,
                new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DateUtils.YYYY_MM_DD_HH_MM_SS)));
        javaTimeModule.addDeserializer(LocalDate.class,
                new LocalDateDeserializer(DateTimeFormatter.ofPattern(DateUtils.YYYY_MM_DD)));
        javaTimeModule.addDeserializer(LocalTime.class,
                new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DateUtils.HH_MM_SS)));

        objectMapper.registerModule(javaTimeModule);
        // 禁用 FAIL_ON_EMPTY_BEANS，允许json转换中的null值
        // 解决报错： Could not write JSON: No serializer found for class java.lang.Object
//        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        return objectMapper;
    }
}
