package com.luckykuang.auth.config.datasource;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Objects;

/**
 * 第三数据源配置
 * @author luckykuang
 * @date 2023/5/28 12:33
 */
@Configuration
@RequiredArgsConstructor
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactoryThird",
        transactionManagerRef = "transactionManagerThird",
        basePackages = {"com.luckykuang.auth.repository.third"})
public class ThirdConfig {

    private final DataSource thirdDataSource;
    private final JpaProperties jpaProperties;
    private final HibernateProperties hibernateProperties;

    @Bean(name = "entityManagerFactoryThird")    //primary实体工厂
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryThird(EntityManagerFactoryBuilder builder) {

        return builder.dataSource(thirdDataSource)
                .properties(getHibernateProperties())
                .packages("com.luckykuang.auth.model.third")     //换成你自己的实体类所在位置
                .persistenceUnit("thirdPersistenceUnit")
                .build();
    }

    @Bean(name = "entityManagerThird")
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return Objects.requireNonNull(entityManagerFactoryThird(builder).getObject()).createEntityManager();
    }

    @Bean(name = "transactionManagerThird")
    public PlatformTransactionManager transactionManager(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(Objects.requireNonNull(entityManagerFactoryThird(builder).getObject()));
    }

    private Map<String, Object> getHibernateProperties() {
        return hibernateProperties.determineHibernateProperties(jpaProperties.getProperties(), new HibernateSettings());
    }
}
