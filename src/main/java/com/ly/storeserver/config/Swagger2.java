package com.ly.storeserver.config;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author ly
 * @Date 2020/3/28
 * @Version V1.0.0
 **/
@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUI
public class Swagger2 {

    /**
     * swagger 配置
     *
     * @return
     */
    @Bean
    public Docket docket() {
        List<ApiKey> apiKeyList = new ArrayList<>();
        apiKeyList.add(new ApiKey("token", "token", "header"));
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        //.termsOfServiceUrl("github.wylwq.store-server")
                        .version("1.0.0")//版本
                        .description("仓库管理系统 API")
                        .title("仓库管理系统后台-接口")
                        .build())
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(apiKeyList);
    }

}

