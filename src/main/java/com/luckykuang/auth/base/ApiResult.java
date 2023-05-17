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

package com.luckykuang.auth.base;

import com.luckykuang.auth.constants.enums.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 统一返回类
 * @author luckykuang
 * @date 2023/4/10 19:47
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
public class ApiResult<T> {

    /**
     * 错误码
     */
    @Schema(description = "错误码")
    private Integer code;
    /**
     * 错误提示，用户可阅读
     */
    @Schema(description = "错误提示")
    private String message;
    /**
     * 返回数据
     */
    @Schema(description = "返回数据")
    private T data;

    public static <T> ApiResult<T> success(){
        return commonResult(ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage(), null);
    }
    public static <T> ApiResult<T> success(T data){
        return commonResult(ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage(), data);
    }
    public static <T> ApiResult<T> failed(ErrorCode errorCode){
        return commonResult(errorCode.getCode(), errorCode.getMessage(),null);
    }
    public static <T> ApiResult<T> failed(Integer code, String message){
        return commonResult(code, message,null);
    }
    public static <T> ApiResult<T> failed(ErrorCode errorCode, T data){
        return commonResult(errorCode.getCode(), errorCode.getMessage(),data);
    }
    private static <T> ApiResult<T> commonResult(Integer code, String message, T data){
        return new ApiResult<>(code,message,data);
    }
}
