package com.ly.storeserver.config;

import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.ly.storeserver.common.enums.DBTypeEnum;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author ly
 * @Date 2020/3/29
 * @Version V1.0.0
 **/
@Slf4j
@Configuration
@MapperScan(basePackages = "com.ly.storeserver.admin.mapper", sqlSessionTemplateRef = "sqlSessionTemplate")
public class MybatisPlusConfig {

    private String mapperLocations = "classpath:com/ly/storeserver/admin/mapper/xml/*.xml";

    /**
     * 乐观锁
     * @return
     */
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }

    /**
     * mybatis-plus分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor page = new PaginationInterceptor();
        page.setDialectType("mysql");
        return page;
    }

    @ConfigurationProperties(prefix = "master.datasource")
    @Bean
    public DataSource masterDataSource() {
        return new HikariDataSource();
    }

    @ConfigurationProperties(prefix = "slave.datasource")
    @Bean
    public DataSource slaveDataSource() {
        return new HikariDataSource();
    }

    @Bean
    public DynamicDataSource dynamicDataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        Map<Object, Object> map = new HashMap<>();
        map.put(DBTypeEnum.MASTER, masterDataSource());
        map.put(DBTypeEnum.SLAVE, slaveDataSource());
        //默认数据源
        dynamicDataSource.setDefaultTargetDataSource(masterDataSource());
        //可配置的数据源
        dynamicDataSource.setTargetDataSources(map);
        return dynamicDataSource;
    }

    /*@Bean(name = "sqlSessionFactory")
    public MybatisSqlSessionFactoryBean sqlSessionFactoryBean(DataSource dynamicDataSource) {
        MybatisSqlSessionFactoryBean mybatisPlus = new MybatisSqlSessionFactoryBean();
        mybatisPlus.setDataSource(dynamicDataSource);
        return mybatisPlus;
    }*/

    @Autowired
    public PaginationInterceptor paginationInterceptor;

    @Autowired
    public OptimisticLockerInterceptor optimisticLockerInterceptor;


    @Bean(name = "mybatisSqlSessionFactoryBean")
    public SqlSessionFactory mybatisSqlSessionFactoryBean(DataSource dynamicDataSource) throws Exception {
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(dynamicDataSource);

        //读取mybatis xml文件位置
        bean.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(mapperLocations));
        Interceptor[] plugins = {paginationInterceptor, optimisticLockerInterceptor};
        bean.setPlugins(plugins);
        return bean.getObject();
    }

    @Bean("sqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("mybatisSqlSessionFactoryBean") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager(DataSource dynamicDataSource) {
        return new DataSourceTransactionManager(dynamicDataSource);
    }

}
