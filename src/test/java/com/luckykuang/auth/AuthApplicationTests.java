package com.luckykuang.auth;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

@Slf4j
@SpringBootTest
class AuthApplicationTests {

    @Test
    void contextLoads(ApplicationContext context) {
        Environment environment = context.getEnvironment();
        String property = environment.getProperty("config.name");
        log.info(property);
    }

}
