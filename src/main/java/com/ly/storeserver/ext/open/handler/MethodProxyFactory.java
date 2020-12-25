package com.ly.storeserver.ext.open.handler;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.ly.storeserver.common.annotation.ApiProxyMethod;
import com.ly.storeserver.common.annotation.ApiProxyService;
import com.ly.storeserver.ext.open.exception.ProxyApiException;
import com.ly.storeserver.ext.open.request.BaseRequest;
import com.ly.storeserver.ext.open.response.BaseResponse;
import com.ly.storeserver.ext.open.response.ReturnCode;
import com.ly.storeserver.utils.AESUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 方法代理工厂
 *
 * @author : wangyu
 * @since :  2020/12/25/025 14:22
 */
@Service
@Slf4j
@Lazy
public class MethodProxyFactory {

    @Autowired
    private ApplicationContext applicationContext;

    private static final Map<String, MethodParam> METHOD_PARAM_MAP = new HashMap<>();

    public BaseResponse invokeProxyService(BaseRequest request) throws InvocationTargetException, IllegalAccessException {
        Long instanceId = request.getInstanceId();
        //通过instanceId查询数据库中是否有该实例对象，如果有就进行下面的处理，没有就直接返回
        Object merchant = this.findMerchantByInstanceId(instanceId);
        if (merchant == null) {
            throw new ProxyApiException(ReturnCode.INTERNAL_SERVER_ERROR.getReturnCode(), "商户实例不存在");
        }
        MethodParam methodParam = METHOD_PARAM_MAP.get(request.getServiceName());
        if (methodParam == null) {
            throw new ProxyApiException(ReturnCode.BAD_REQUEST);
        }
        Object requestBody;
        try {
            requestBody = JSON.parseObject(request.getBody(), methodParam.getRequestType());
            log.info("请求参数如下：{}", requestBody);
        } catch (Exception e) {
            throw new ProxyApiException(ReturnCode.BAD_REQUEST);
        }
        Object bean = methodParam.getBean();
        Method method = methodParam.getMethod();
        Object invoke = method.invoke(bean, "商户id", requestBody);
        //这里如果商户自己配置数据的加密密钥则需要商户自己的密钥
        return new BaseResponse(AESUtil.encrypt(JSONUtil.toJsonStr(invoke)));
    }

    public Object findMerchantByInstanceId(Long instaceId) {
        //查询数据库
        return new Object();
    }

    /**
     * 初始化代理方法
     */
    @PostConstruct
    public void init(){
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(ApiProxyService.class);
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            if (entry.getValue().getClass().isAnnotationPresent(ApiProxyService.class)) {
                Method[] methods = ReflectUtil.getMethods(entry.getValue().getClass());
                for (Method method : methods) {
                    if (!method.isAnnotationPresent(ApiProxyMethod.class)) {
                        continue;
                    }
                    ApiProxyMethod annotation = method.getAnnotation(ApiProxyMethod.class);
                    Class requestType = annotation.requestType();
                    String serviceName = annotation.serviceName();
                    MethodParam methodParam = new MethodParam();
                    methodParam.setBean(entry.getValue());
                    methodParam.setMethod(method);
                    methodParam.setRequestType(requestType);
                    methodParam.setServiceName(serviceName);
                    METHOD_PARAM_MAP.put(serviceName, methodParam);
                }
            }
        }
    }
}
