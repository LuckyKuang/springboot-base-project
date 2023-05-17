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

package com.luckykuang.auth.service;

import com.luckykuang.auth.config.EasyCaptchaConfig;
import com.luckykuang.auth.constants.RedisConstants;
import com.luckykuang.auth.exception.BusinessException;
import com.luckykuang.auth.response.CaptchaRsp;
import com.luckykuang.auth.utils.CommonUtils;
import com.luckykuang.auth.utils.RedisUtils;
import com.wf.captcha.*;
import com.wf.captcha.base.Captcha;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.*;

/**
 * @author fankuangyong
 * @date 2023/5/17 10:55
 */

@Service
@RequiredArgsConstructor
public class EasyCaptchaService {

    private final EasyCaptchaConfig easyCaptchaConfig;
    private final RedisUtils redisUtils;

    public CaptchaRsp getCaptcha() {
        // 生成验证码
        Captcha captcha = generateCaptcha();
        String captchaText = captcha.text(); // 验证码文本
        String captchaBase64 = captcha.toBase64(); // 验证码图片Base64字符串

        String captchaKey = CommonUtils.getUUID(true);
        // 验证码文本缓存至Redis，用于登录校验
        redisUtils.set(
                RedisConstants.REDIS_HEAD + RedisConstants.CAPTCHA_KEY + captchaKey,
                captchaText,
                easyCaptchaConfig.getTtl());

        return CaptchaRsp.from(captchaKey, captchaBase64);
    }

    private Captcha generateCaptcha() {
        Captcha captcha;
        int width = easyCaptchaConfig.getWidth();
        int height = easyCaptchaConfig.getHeight();
        int length = easyCaptchaConfig.getLength();
        String fontName = easyCaptchaConfig.getFontName();

        switch (easyCaptchaConfig.getType()) {
            case ARITHMETIC -> {
                captcha = new ArithmeticCaptcha(width, height);
                //固定设置为两位，图片为算数运算表达式
                captcha.setLen(2);
            }
            case CHINESE -> {
                captcha = new ChineseCaptcha(width, height);
                captcha.setLen(length);
            }
            case CHINESE_GIF -> {
                captcha = new ChineseGifCaptcha(width, height);
                captcha.setLen(length);
            }
            case GIF -> {
                captcha = new GifCaptcha(width, height);//最后一位是位数
                captcha.setLen(length);
            }
            case SPEC -> {
                captcha = new SpecCaptcha(width, height);
                captcha.setLen(length);
            }
            default -> throw new BusinessException("验证码配置信息错误");
        }
        captcha.setFont(new Font(fontName, easyCaptchaConfig.getFontStyle(), easyCaptchaConfig.getFontSize()));
        return captcha;

    }
}
