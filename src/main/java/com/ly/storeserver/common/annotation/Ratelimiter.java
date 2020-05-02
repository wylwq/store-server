package com.ly.storeserver.common.annotation;

import java.lang.annotation.*;

/**
 * @Description: 通过注解实现限流
 * @Author ly
 * @Date 2020/4/2 7:27
 * @Version V1.0.0
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Ratelimiter {

    /**
     * 以每秒固定的速率向桶中加令牌
     */
    double limit();

    /**
     * 在规定的时间内如果没有令牌，就走服务降级处理
     */
    long timeout();

}
