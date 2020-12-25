package com.ly.storeserver.common.annotation;

import java.lang.annotation.*;

/**
 * open-api代理服务
 *
 * @author : wangyu
 * @since :  2020/12/25/025 13:38
 */
@Target({ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface ApiProxyService {
}
