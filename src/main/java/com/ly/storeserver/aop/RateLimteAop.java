package com.ly.storeserver.aop;

import com.google.common.util.concurrent.RateLimiter;
import com.ly.storeserver.common.annotation.Ratelimiter;
import com.ly.storeserver.common.bean.R;
import com.ly.storeserver.common.enums.RStatus;
import com.ly.storeserver.exception.ServiceException;
import com.ly.storeserver.utils.CodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 采用aop的方式实现限流
 * @Author ly
 * @Date 2020/4/2 7:35
 * @Version V1.0.0
 **/
@Aspect
@Component
@Slf4j
public class RateLimteAop extends BaseAop{

    private Map<String, RateLimiter> curMap = new ConcurrentHashMap<>();

    @Pointcut("execution(public * com.ly.storeserver.admin.controller.*.*(..)) " +
              "|| execution(public * com.ly.storeserver.ext.open.controller.*.*(..))")
    public void roundLimitAop() {

    }

    @Around(value = "roundLimitAop()")
    public Object doBefore(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        //1.判断请求方法上是否含有@Ratelimter注解
        Method method = getMethod(proceedingJoinPoint);
        if (method == null) {
            return null;
        }
        Ratelimiter ratelimiter = method.getDeclaredAnnotation(Ratelimiter.class);
        if (ratelimiter == null) {
            //直接就如实际请求中
            return proceedingJoinPoint.proceed();
        }
        //2.调用原生的Ratelimit创建令牌
        double limit = ratelimiter.limit();
        long timeout = ratelimiter.timeout();
        String requestURI = getRequestURI();
        RateLimiter rateLimiter = curMap.get(requestURI);
        if (null == rateLimiter) {
            rateLimiter = RateLimiter.create(limit);
            curMap.put(requestURI, rateLimiter);
        }
        boolean tryAcquire = rateLimiter.tryAcquire(timeout, TimeUnit.MILLISECONDS);
        if (!tryAcquire) {
            log.error(RStatus.REQ_LIMIT.getMessage());
            throw new ServiceException(RStatus.REQ_LIMIT);
        }
        return proceedingJoinPoint.proceed();
    }

    @Deprecated
    private void fallBack() {
        HttpServletResponse response = getResponse();
        response.setHeader("Content-Type", "text/html;charset=utf-8");
        try(PrintWriter writer = response.getWriter()) {
            writer.println(new R<>(RStatus.REQ_LIMIT));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getRequestURI() {
        String requestURI = getRequest().getRequestURI();
        String ipAddr = CodeUtil.getIpAddr(getRequest());
        StringBuffer path = new StringBuffer();
        path.append(ipAddr).append(":").append(requestURI);
        return path.toString();
    }
}
