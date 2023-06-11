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

package com.luckykuang.auth.security.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.*;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.luckykuang.auth.constants.enums.ErrorCode;
import com.luckykuang.auth.exception.BusinessException;
import com.luckykuang.auth.security.properties.TokenSecretProperties;
import com.luckykuang.auth.security.userdetails.LoginUserDetails;
import com.luckykuang.auth.utils.CommonUtils;
import com.luckykuang.auth.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * jwt有以下7个官方字段供选择:
 *      iss(issuer)：签发人
 *      exp(expiration time)：过期时间
 *      sub(subject)：主题
 *      aud(audience)：受众
 *      nbf(not before)：生效时间
 *      iat(issued at)：签发时间
 *      jti(jwt id)：编号
 * jwt预留自定义参数：
 *      Claim(String name, Object value)
 * 本系统设计：
 *      sub -> username 用户名
 *      Claim(userId, value) -> userId 用户id
 *      Claim(deptId, value) -> deptId 部门id
 *      Claim(dataScope, value) -> dataScope 数据权限
 *      Claim(authorities, value) -> authorities 角色
 *
 * @author luckykuang
 * @date 2023/4/22 17:57
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final TokenSecretProperties tokenSecretProperties;
    private final RedisUtils redisUtils;

    /**
     * 生成验证令牌
     * @param authentication
     * @return AccessToken
     */
    public String generateAccessToken(Authentication authentication){
        return createToken(authentication, tokenSecretProperties.getAccessTokenExpirationDate());
    }

    /**
     * 生成刷新令牌
     * @param authentication
     * @return 刷新令牌
     */
    public String generateRefreshToken(Authentication authentication){
        return createToken(authentication, tokenSecretProperties.getRefreshTokenExpirationDate());
    }

    /**
     * 获取用户id
     * @param token
     * @return
     */
    public Long getUserId(String token){
        return getDecodedJWT(token).getClaim("userId").asLong();
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
            throw new BusinessException(ErrorCode.TOKEN_ILLEGAL);
        } catch (SignatureVerificationException e){
            log.warn("令牌验证失败：[{}]",token);
            throw new BusinessException(ErrorCode.TOKEN_VERIFICATION_FAILED);
        } catch (TokenExpiredException e){
            log.info("令牌已过期：[{}]",token);
            throw new BusinessException(ErrorCode.TOKEN_INVALID);
        } catch (IncorrectClaimException e){
            log.warn("令牌不正确：[{}]",token);
            throw new BusinessException(ErrorCode.TOKEN_INCORRECT);
        }
        return verify;
    }

    /**
     * 获取认证信息
     * @param token
     * @return
     */
    public Authentication getAuthentication(String token){

        Boolean tokenExpired = isTokenExpired(token);
        // token 过期
        if (Boolean.TRUE.equals(tokenExpired)){
            throw new BusinessException(ErrorCode.TOKEN_INVALID);
        }

        LoginUserDetails principal = new LoginUserDetails();

        DecodedJWT decodedJWT = getDecodedJWT(token);
        principal.setUserId(decodedJWT.getClaim("userId").asLong());
        principal.setUsername(decodedJWT.getSubject());
        principal.setDeptId(decodedJWT.getClaim("deptId").asLong());
        principal.setDataScope(decodedJWT.getClaim("dataScope").asInt());

        List<SimpleGrantedAuthority> authorities = decodedJWT.getClaim("authorities").asList(String.class)
                .stream().map(SimpleGrantedAuthority::new)
                .toList();
        return new UsernamePasswordAuthenticationToken(principal, null,authorities);
    }

    private Boolean isTokenExpired(String token) {
        return getJwtExpirationDate(token).before(new Date());
    }

    private String createToken(Authentication authentication, long jwtExpirationDate) {
        String sign;
        try {
            LoginUserDetails principal = (LoginUserDetails) authentication.getPrincipal();
            List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

            sign = JWT.create()
                    .withJWTId(CommonUtils.getUUID(true))
                    .withSubject(authentication.getName()) // username
                    .withIssuedAt(new Date(System.currentTimeMillis()))
                    .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpirationDate))
                    .withClaim("userId",principal.getUserId())
                    .withClaim("deptId",principal.getDeptId())
                    .withClaim("dataScope",principal.getDataScope())
                    .withClaim("authorities",roles)
                    .sign(getKey());
        } catch (JWTCreationException e){
            log.error("令牌创建异常 -> 用户名：[{}]",authentication.getName(),e);
            throw new BusinessException(ErrorCode.TOKEN_CREATE_EXCEPTION);
        }
        return sign;
    }

    /**
     * 加密
     * 生产环境建议修改成RSA加密
     */
    private Algorithm getKey(){
        return Algorithm.HMAC256(tokenSecretProperties.getSecret());
    }
}
