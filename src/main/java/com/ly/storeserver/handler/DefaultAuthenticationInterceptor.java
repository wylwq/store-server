package com.ly.storeserver.handler;

import com.ly.storeserver.common.annotation.Token;
import com.ly.storeserver.common.enums.RStatus;
import com.ly.storeserver.exception.ServiceException;
import com.ly.storeserver.utils.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @Description:
 * @Author ly
 * @Date 2020/4/2 23:21
 * @Version V1.0.0
 **/
public class DefaultAuthenticationInterceptor implements HandlerInterceptor {

    private JwtTokenUtil jwtTokenUtil;

    public DefaultAuthenticationInterceptor(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return getUser(request, handler);
    }

    /**
     * 登录校验
     * @param request
     * @param handler
     * @return
     */
    private boolean getUser(HttpServletRequest request, Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        String token = request.getHeader("token");
        if (StringUtils.isBlank(token)) {
            token = request.getParameter("token");
        }
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        if (request.getRequestURI().contains("swagger") || request.getRequestURI().contains("error")) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        //白名单校验
        if (passToken(method)) {
            return true;
        }
        if (StringUtils.isBlank(token)) {
            throw new ServiceException("请登录", RStatus.NO_LOGIN);
        }

        Claims claims;
        try {
            claims = jwtTokenUtil.getClaimFromToken(token);
        } catch (JwtException j) {
            throw new ServiceException(RStatus.NO_LOGIN);
        }
        request.setAttribute("token", token);
        request.setAttribute("userMobile", claims.get("userMobile"));
        return true;
    }

    /**
     * 如果指定方法上面加了Token注解，并且不是需要拦截的方法那么就放行
     * @param method
     * @return
     */
    private boolean passToken(Method method) {
        if (method.isAnnotationPresent(Token.class)) {
            Token passToken = method.getAnnotation(Token.class);
            if (passToken != null && !passToken.required()) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
