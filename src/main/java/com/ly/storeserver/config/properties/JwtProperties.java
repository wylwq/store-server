package com.ly.storeserver.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description:
 * @Author ly
 * @Date 2020/3/31 22:26
 * @Version V1.0.0
 **/
@Data
@ConfigurationProperties(prefix = "lybs.jwt")
public class JwtProperties {

    /**
     * jwt秘钥
     */
    private String secret;

    /**
     * jwt过期时间，单位：秒
     */
    private Long expiration;
}
