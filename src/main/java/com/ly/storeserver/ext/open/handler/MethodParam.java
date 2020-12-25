package com.ly.storeserver.ext.open.handler;

import lombok.Data;

import java.lang.reflect.Method;

/**
 * 方法参数
 *
 * @author : wangyu
 * @since :  2020/12/25/025 14:18
 */
@Data
public class MethodParam {

    /**
     * 该方法的实例
     */
    private Object bean;

    /**
     * 请求参数类型
     */
    private Class<?> requestType;

    /**
     * 服务的名称
     */
    private String serviceName;

    /**
     * 被调用的方法
     */
    private Method method;
}
