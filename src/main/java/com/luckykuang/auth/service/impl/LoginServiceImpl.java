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

package com.luckykuang.auth.service.impl;

import com.luckykuang.auth.base.ApiResult;
import com.luckykuang.auth.constants.RedisConstants;
import com.luckykuang.auth.constants.enums.ErrorCode;
import com.luckykuang.auth.exception.BusinessException;
import com.luckykuang.auth.properties.EasyCaptchaProperties;
import com.luckykuang.auth.request.LoginReq;
import com.luckykuang.auth.response.CaptchaRsp;
import com.luckykuang.auth.response.TokenRsp;
import com.luckykuang.auth.security.properties.TokenSecretProperties;
import com.luckykuang.auth.security.utils.JwtTokenProvider;
import com.luckykuang.auth.service.LoginService;
import com.luckykuang.auth.utils.CommonUtils;
import com.luckykuang.auth.utils.RedisUtils;
import com.luckykuang.auth.utils.RequestUtils;
import com.wf.captcha.*;
import com.wf.captcha.base.Captcha;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.awt.*;

/**
 * @author fankuangyong
 * @date 2023/5/18 17:13
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final TokenSecretProperties tokenSecretProperties;
    private final EasyCaptchaProperties easyCaptchaProperties;
    private final RedisUtils redisUtils;

    @Override
    public ApiResult<TokenRsp> login(LoginReq loginReq) {
        LoginReq from = LoginReq.from(loginReq);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                from.username(),
                from.password()
        );
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        String accessToken = jwtTokenProvider.generateAccessToken(authenticate);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authenticate);
        // 加入redis缓存
        redisUtils.set(
                RedisConstants.REDIS_HEAD + RedisConstants.ACCESS_TOKEN + accessToken,
                refreshToken,
                tokenSecretProperties.getJwtAccessTokenExpirationDate() / 1000
        );
        return ApiResult.success(TokenRsp.from(accessToken,refreshToken));
    }

    @Override
    public ApiResult<TokenRsp> refresh(HttpServletRequest request) {
        String token = RequestUtils.resolveToken(request);
        if (StringUtils.isNotBlank(token) && SecurityContextHolder.getContext().getAuthentication() == null){
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            String accessToken = jwtTokenProvider.generateAccessToken(authentication);
            String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);
            // 加入redis缓存
            redisUtils.set(
                    RedisConstants.REDIS_HEAD + RedisConstants.ACCESS_TOKEN + accessToken,
                    refreshToken,
                    tokenSecretProperties.getJwtAccessTokenExpirationDate() / 1000
            );
            return ApiResult.success(TokenRsp.from(accessToken,refreshToken));
        }
        return ApiResult.success(TokenRsp.from("",""));
    }

    @Override
    public ApiResult<Void> logout(HttpServletRequest request) {
        String token = RequestUtils.resolveToken(request);
        log.info("logout token:{}",token);
        if (StringUtils.isNotBlank(token) && SecurityContextHolder.getContext().getAuthentication() == null){
            Long userId = jwtTokenProvider.getUserId(token);
            // 删除token redis缓存
            redisUtils.del(RedisConstants.REDIS_HEAD + RedisConstants.ACCESS_TOKEN + token);
            // 删除菜单权限
            redisUtils.del(RedisConstants.REDIS_HEAD + RedisConstants.USER_MENU_KEY + userId);
            // 清除security缓存
            SecurityContextHolder.clearContext();
        }
        return ApiResult.success();
    }

    @Override
    public CaptchaRsp getCaptcha() {
        // 生成验证码
        Captcha captcha = generateCaptcha();
        String captchaText = captcha.text(); // 验证码文本
        String captchaBase64 = captcha.toBase64(); // 验证码图片Base64字符串

        String captchaKey = CommonUtils.getUUID(true);
        // 验证码文本缓存至Redis，用于登录校验
        redisUtils.set(
                RedisConstants.REDIS_HEAD + RedisConstants.CAPTCHA_CACHE_KEY + captchaKey,
                captchaText,
                easyCaptchaProperties.getTtl());

        return CaptchaRsp.from(captchaKey, captchaBase64);
    }

    private Captcha generateCaptcha() {
        Captcha captcha;
        int width = easyCaptchaProperties.getWidth();
        int height = easyCaptchaProperties.getHeight();
        int length = easyCaptchaProperties.getLength();
        String fontName = easyCaptchaProperties.getFontName();

        switch (easyCaptchaProperties.getType()) {
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
            default -> throw new BusinessException(ErrorCode.CAPTCHA_CONFIG_ERROR);
        }
        captcha.setFont(new Font(fontName, easyCaptchaProperties.getFontStyle(), easyCaptchaProperties.getFontSize()));
        return captcha;
    }
}
