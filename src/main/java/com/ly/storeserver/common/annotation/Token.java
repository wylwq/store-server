package com.ly.storeserver.common.annotation;

import java.lang.annotation.*;

/**
 * @Description:
 * @Author ly
 * @Date 2020/4/2 23:40
 * @Version V1.0.0
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Token {

    boolean required() default false;
}
