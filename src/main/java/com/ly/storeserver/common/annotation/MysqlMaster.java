package com.ly.storeserver.common.annotation;

import java.lang.annotation.*;

/**
 * @Description: mysql数据库master节点
 * @Author ly
 * @Date 2020/5/17 21:26
 * @Version V1.0.0
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MysqlMaster {
}
