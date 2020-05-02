package com.ly.storeserver.config;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.ClassicConfiguration;
import org.flywaydb.core.api.configuration.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author ly
 * @Date 2020/4/3 13:40
 * @Version V1.0.0
 **/
@Component
public class FlywayConfig {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Bean
    public Configuration configuration() {
        ClassicConfiguration configuration = new ClassicConfiguration();
        configuration.setDataSource(url, username, password);
        return configuration;
    }

    @Bean
    public Flyway flyway(Configuration configuration) {
        Flyway flyway = new Flyway(configuration);
        return flyway;
    }

    //生成FlywayMigrationInitializer的实例
    //这个类实现了InitalizingBean接口，可以在依赖注入完成后执行一些操作
    @Bean
    @ConditionalOnMissingBean
    public FlywayMigrationInitializer flywayInitializer(Flyway flyway) {
        return new FlywayMigrationInitializer(flyway);
    }

}
