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

package com.luckykuang.auth.config;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.interceptor.CustomizableTraceInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 方法出入参截取打印
 * @author luckykuang
 * @date 2023/4/27 15:52
 */
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Aspect
@Configuration
public class TraceAdvisorConfig {

    /**
     * 需要在配置文件里面将 com.luckykuang.auth.controller 包下面的日志级别设置成trace
     */
    @Pointcut("execution(* com.luckykuang.auth.controller..*.*(..))")
    public void monitor(){}

    /**
     * 监控追踪
     */
    @Bean
    public Advisor traceAdvisor(){
        CustomizableTraceInterceptor interceptor = new CustomizableTraceInterceptor();
        interceptor.setEnterMessage("请求参数 $[targetClassShortName]: $[methodName]($[arguments])");
        interceptor.setExitMessage("响应参数 $[targetClassShortName]: $[methodName]: $[returnValue]");
        interceptor.setExceptionMessage("响应异常 $[targetClassShortName]: $[methodName]: $[exception]");
        interceptor.setUseDynamicLogger(true);
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("com.luckykuang.auth.config.TraceAdvisorConfig.monitor()");
        return new DefaultPointcutAdvisor(pointcut,interceptor);
    }
}
