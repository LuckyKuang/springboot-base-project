package com.luckykuang.auth.config.aop;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.util.Collections;

/**
 * 全局事务配置-只支持单一数据源
 * @author luckykuang
 * @date 2023/6/11 14:11
 */
@Aspect
@Configuration
@RequiredArgsConstructor
public class TransactionAdvisorConfig {
    /**
     * 切入点
     */
    private static final String POINTCUT_EXPRESSION = "execution(* com.luckykuang.auth.service.impl..*.*(..))";

    /**
     * 注入事务管理器
     */
    private final TransactionManager transactionManager;

    /**
     * 定义事务增强
     * @return TransactionInterceptor
     */
    @Bean
    public TransactionInterceptor txAdvice() {
        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
        // 查询
        RuleBasedTransactionAttribute txAttributeRequiredReadOnly = new RuleBasedTransactionAttribute();
        // 事务默认级别：如果不存在，请创建一个新的。
        txAttributeRequiredReadOnly.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        // 只读，不做更新操作
        txAttributeRequiredReadOnly.setReadOnly(true);
        // 匹配上如下方法名开头，都算是查询
        //【切记代码规约要做好】
        source.addTransactionalMethod("get*", txAttributeRequiredReadOnly);
        source.addTransactionalMethod("select*", txAttributeRequiredReadOnly);
        source.addTransactionalMethod("query*", txAttributeRequiredReadOnly);
        source.addTransactionalMethod("load*", txAttributeRequiredReadOnly);
        source.addTransactionalMethod("search*", txAttributeRequiredReadOnly);
        source.addTransactionalMethod("find*", txAttributeRequiredReadOnly);
        source.addTransactionalMethod("list*", txAttributeRequiredReadOnly);
        source.addTransactionalMethod("count*", txAttributeRequiredReadOnly);
        source.addTransactionalMethod("is*", txAttributeRequiredReadOnly);

        // 增删改
        RuleBasedTransactionAttribute txAttributeRequired = new RuleBasedTransactionAttribute();
        // 事务默认级别：如果不存在，请创建一个新的。
        // 如果需要用到其他级别，请再方法上添加注解@Transactional覆盖
        txAttributeRequired.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        // 设置异常对象回滚
        txAttributeRequired.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
        // 匹配上如下方法名开头，都算是增删改
        //【切记代码规约要做好】
        source.addTransactionalMethod("add*", txAttributeRequired);
        source.addTransactionalMethod("create*", txAttributeRequired);
        source.addTransactionalMethod("save*", txAttributeRequired);
        source.addTransactionalMethod("insert*", txAttributeRequired);
        source.addTransactionalMethod("modify*", txAttributeRequired);
        source.addTransactionalMethod("update*", txAttributeRequired);
        source.addTransactionalMethod("del*", txAttributeRequired);
        source.addTransactionalMethod("delete*", txAttributeRequired);
        source.addTransactionalMethod("remove*", txAttributeRequired);

        return new TransactionInterceptor(transactionManager, source);
    }

    /**
     * 织入事务
     * @return Advisor
     */
    @Bean
    public Advisor txAdviceAdvisor() {
        // 切入点
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(POINTCUT_EXPRESSION);
        return new DefaultPointcutAdvisor(pointcut, txAdvice());
    }
}
