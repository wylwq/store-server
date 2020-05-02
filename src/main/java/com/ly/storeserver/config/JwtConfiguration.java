package com.ly.storeserver.config;

import com.ly.storeserver.config.properties.JwtProperties;
import com.ly.storeserver.utils.JwtTokenUtil;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:
 * @Author ly
 * @Date 2020/3/31 22:30
 * @Version V1.0.0
 **/
@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class JwtConfiguration {


    /**
     * jwt工具类
     */
    @Bean
    public JwtTokenUtil jwtTokenUtil(JwtProperties jwtProperties) {
        return new JwtTokenUtil(jwtProperties.getSecret(), jwtProperties.getExpiration());
    }
}
