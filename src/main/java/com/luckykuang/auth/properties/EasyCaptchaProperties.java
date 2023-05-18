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

package com.luckykuang.auth.properties;

import com.luckykuang.auth.constants.enums.CaptchaTypeEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.awt.*;

/**
 * @author fankuangyong
 * @date 2023/5/17 10:50
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "easy-captcha")
public class EasyCaptchaProperties {

    /**
     * 验证码类型
     */
    private CaptchaTypeEnum type = CaptchaTypeEnum.ARITHMETIC;

    /**
     * 验证码缓存过期时间(单位:秒)
     */
    private long ttl = 120L;

    /**
     * 验证码内容长度
     */
    private int length = 4;

    /**
     * 验证码宽度
     */
    private int width = 120;

    /**
     * 验证码高度
     */
    private int height = 36;

    /**
     * 验证码字体
     */
    private String fontName = "Verdana";

    /**
     * 字体风格
     */
    private int fontStyle = Font.PLAIN;

    /**
     * 字体大小
     */
    private int fontSize = 20;
}
