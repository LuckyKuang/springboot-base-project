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

package com.luckykuang.auth.utils;

import java.util.UUID;

/**
 * @author fankuangyong
 * @date 2023/5/17 15:46
 */
public class CommonUtils {

    private CommonUtils(){}

    /**
     * 获取去除"-"后的uuid
     * @param flag true-获取去除"-"后的uuid  false-获取原始uuid
     * @return
     */
    public static String getUUID(boolean flag){
        if (flag){
            return UUID.randomUUID().toString().replace("-","");
        }
        return UUID.randomUUID().toString();
    }
}
