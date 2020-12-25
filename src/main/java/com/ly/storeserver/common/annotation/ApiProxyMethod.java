package com.ly.storeserver.common.annotation;

import java.lang.annotation.*;

/**
 * open-api代理方法
 *
 * @author : wangyu
 * @since :  2020/12/25/025 13:40
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiProxyMethod {

    /**
     *接口请求类型
     * @return
     */
    Class requestType();

    /**
     * 服务方法名称
     * @return
     */
    String serviceName();
}
