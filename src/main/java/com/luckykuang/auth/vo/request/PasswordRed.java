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

package com.luckykuang.auth.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * @author luckykuang
 * @date 2023/5/26 16:52
 */
public record PasswordRed(
        @NotBlank
        @Pattern(regexp = "^.*(?=.{6,})(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#&*?_-]).*$",
                message = "密码最少6位，且包括至少1个大写字母，1个小写字母，1个数字，1个特殊字符[!@#&*?_-]")
        @Schema(description = "原密码")
        String oldPassword,
        @NotBlank
        @Pattern(regexp = "^.*(?=.{6,})(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#&*?_-]).*$",
                message = "密码最少6位，且包括至少1个大写字母，1个小写字母，1个数字，1个特殊字符[!@#&*?_-]")
        @Schema(description = "新密码")
        String newPassword,
        @NotBlank
        @Schema(description = "确认新密码")
        String confirmNewPassword) {
}
