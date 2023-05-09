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

package com.luckykuang.auth.exception;

import com.luckykuang.auth.enums.ErrorCode;
import lombok.Getter;

/**
 * @author luckykuang
 * @date 2023/4/21 15:12
 */
@Getter
public class BusinessException extends RuntimeException{
    /**
     * 错误码
     */
    private final Integer code;
    /**
     * 错误提示
     */
    private final String message;

    public BusinessException(ErrorCode errorCode){
        super();
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    public BusinessException(String message){
        super();
        this.code = ErrorCode.INTERNAL_SERVER_ERROR.getCode();
        this.message = message;
    }
}
