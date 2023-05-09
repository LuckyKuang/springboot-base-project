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

package com.luckykuang.auth.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.*;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.luckykuang.auth.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import static com.luckykuang.auth.constants.CoreConstants.AUTHORITIES;

/**
 * @author luckykuang
 * @date 2023/4/22 17:57
 */
@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${app.jwt-secret:luckykuang}")
    private String jwtSecret;
    @Value("${app.jwt-expiration.access-token-milliseconds:10000}")
    private long jwtAccessTokenExpirationDate;
    @Value("${app.jwt-expiration.refresh-token-milliseconds:100000}")
    private long jwtRefreshTokenExpirationDate;

    /**
     * 生成令牌
     * @param userName
     * @return AccessToken
     */
    public String generateToken(String userName){
        return createToken(userName,null,jwtAccessTokenExpirationDate);
    }

    /**
     * 生成验证令牌
     * @param authentication
     * @return AccessToken
     */
    public String generateAccessToken(Authentication authentication){
        return createToken(authentication.getName(),authentication.getAuthorities(),jwtAccessTokenExpirationDate);
    }

    /**
     * 生成刷新令牌
     * @param authentication
     * @return 刷新令牌
     */
    public String generateRefreshToken(Authentication authentication){
        return createToken(authentication.getName(),authentication.getAuthorities(),jwtRefreshTokenExpirationDate);
    }

    /**
     * 获取用户名
     * @param token
     * @return
     */
    public String getUsername(String token) {
        return getDecodedJWT(token).getSubject();
    }

    /**
     * 获取过期时间
     * @param token
     * @return
     */
    public Date getJwtExpirationDate(String token) {
        return getDecodedJWT(token).getExpiresAt();
    }

    /**
     * 校验 token 是否过期
     * @param token
     * @param userDetails
     * @return true未过期，false已过期
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * 获取所有的 jwt 信息
     * @param token 令牌
     * @return
     */
    public DecodedJWT getDecodedJWT(String token) {
        DecodedJWT verify;
        try {
            verify = JWT.require(getKey()).build().verify(token);
        } catch (AlgorithmMismatchException e){
            log.warn("令牌非法：[{}]",token);
            throw new BusinessException("令牌非法");
        } catch (SignatureVerificationException e){
            log.warn("令牌无效：[{}]",token);
            throw new BusinessException("令牌无效");
        } catch (TokenExpiredException e){
            log.warn("令牌已过期：[{}]",token);
            throw new BusinessException("令牌已过期");
        } catch (IncorrectClaimException e){
            log.warn("令牌不正确：[{}]",token);
            throw new BusinessException("令牌不正确");
        }
        return verify;
    }

    private Boolean isTokenExpired(String token) {
        return getJwtExpirationDate(token).before(new Date());
    }

    private String createToken(String userName, Collection<? extends GrantedAuthority> authorities, long jwtExpirationDate) {
        String sign;
        List<String> stringList = authorities == null
                ? null
                : authorities.stream().map(GrantedAuthority::getAuthority).toList();
        try {
            sign = JWT.create()
                    .withSubject(userName)
                    .withIssuedAt(new Date(System.currentTimeMillis()))
                    .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpirationDate))
                    .withClaim(AUTHORITIES, stringList)
                    .sign(getKey());
        } catch (JWTCreationException e){
            log.error("令牌创建异常 -> 用户名：[{}]，权限：[{}]",userName,stringList,e);
            throw new BusinessException("令牌创建异常");
        }
        return sign;
    }

    /**
     * 加密
     * 生产环境建议修改成RSA加密
     */
    private Algorithm getKey(){
        return Algorithm.HMAC256(jwtSecret);
    }
}
