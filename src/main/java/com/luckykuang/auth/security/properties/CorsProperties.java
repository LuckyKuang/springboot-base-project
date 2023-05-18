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

package com.luckykuang.auth.security.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * cors配置
 * @author fankuangyong
 * @date 2023/5/18 17:03
 */
@Getter
@Setter
@Configuration
public class CorsProperties {
    @Value("#{'${app.cors-urls}'.split(',')}")
    private List<String> corsUrls;
    @Value("#{'${app.cors-methods}'.split(',')}")
    private List<String> corsMethods;
}
