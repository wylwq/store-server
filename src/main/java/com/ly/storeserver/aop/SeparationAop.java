package com.ly.storeserver.aop;

import com.ly.storeserver.config.DbConfig;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author ly
 * @Date 2020/5/17 21:09
 * @Version V1.0.0
 **/
@Aspect
@Component
@Slf4j
public class SeparationAop extends BaseAop{

    /**
     * 解释这样写的原因，特殊情况需要查询主库
     */
    @Pointcut(value = "@annotation(com.ly.storeserver.common.annotation.MysqlSlave) " +
            "&& (execution(* com.ly.storeserver.admin.service.impl.*.select*(..)) " +
            "|| execution(* com.ly.storeserver.admin.service.impl.*.find*(..)))")
    public void readPointcut() {

    }

    @Pointcut(value = "@annotation(com.ly.storeserver.common.annotation.MysqlMaster) " +
            "|| execution(* com.ly.storeserver.admin.service.impl.*.insert*(..)) " +
            "|| execution(* com.ly.storeserver.admin.service.impl.*.add*(..)) " +
            "|| execution(* com.ly.storeserver.admin.service.impl.*.update*(..)) " +
            "|| execution(* com.ly.storeserver.admin.service.impl.*.edit*(..)) " +
            "|| execution(* com.ly.storeserver.admin.service.impl.*.delete*(..)) " +
            "|| execution(* com.ly.storeserver.admin.service.impl.*.remove*(..))")
    public void writePointcut() {

    }
    
    @Before(value = "writePointcut()")
    public void write() {
        DbConfig.master();
    }

    @Before(value = "readPointcut()")
    public void read() {
        DbConfig.slave();
    }

}
