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

package com.luckykuang.auth.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author fankuangyong
 * @date 2023/5/17 11:19
 */
public record CaptchaRsp(@Schema(description = "验证码key") String captchaKey,
                         @Schema(description = "验证码base64") String captchaBase64) {
    public static CaptchaRsp from(String captchaKey, String captchaBase64){
        return new CaptchaRsp(captchaKey,captchaBase64);
    }
}
