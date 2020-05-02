package com.ly.storeserver.config;

import com.ly.storeserver.handler.DefaultAuthenticationInterceptor;
import com.ly.storeserver.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Description:
 * @Author ly
 * @Date 2020/4/2 23:06
 * @Version V1.0.0
 **/
@Component
public class WebConfig implements WebMvcConfigurer {

    /**
     * 处理跨域问题
     * @return
     */
    @Bean
    @Profile(value = {"dev", "prod"})
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        //Cors配置对所有接口都有效
        source.registerCorsConfiguration("/**", corsConfiguration);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(0);
        return bean;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(defaultAuthenticationInterceptor());
    }

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Bean
    public DefaultAuthenticationInterceptor defaultAuthenticationInterceptor() {
        return new DefaultAuthenticationInterceptor(jwtTokenUtil);
    }


}
