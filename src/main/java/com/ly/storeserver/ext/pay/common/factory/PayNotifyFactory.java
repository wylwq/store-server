package com.ly.storeserver.ext.pay.common.factory;

import com.ly.storeserver.exception.ServiceException;
import com.ly.storeserver.ext.pay.common.constant.ServiceCode;
import com.ly.storeserver.ext.pay.common.constant.SupplierCode;
import com.ly.storeserver.ext.pay.common.handle.PayNotifyHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 支付工厂，创建支付实例
 *
 * @author : wangyu
 * @since :  2020/12/31/031 16:07
 */
@Slf4j
@Service
public class PayNotifyFactory implements ApplicationContextAware {

    private Map<SupplierCode, Map<ServiceCode,PayNotifyHandler>> handlerMap = new HashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<SupplierCode, Map<ServiceCode,PayNotifyHandler>> handlerMap = new HashMap<>();
        Map<String,PayNotifyHandler> beansMap = applicationContext.getBeansOfType(PayNotifyHandler.class);
        if(beansMap != null){
            beansMap.values().forEach(supplierHandler -> {
                SupplierCode supplierCode = supplierHandler.supplier();
                ServiceCode serviceCode = supplierHandler.service();
                if(supplierCode == null){
                    log.error("payNotifyFactory init error. {} supplierCode is null",supplierHandler.getClass());
                    return;
                }
                if(serviceCode == null){
                    log.error("payNotifyFactory init error. {} serviceCode is null",supplierHandler.getClass());
                    return;
                }
                Map<ServiceCode, PayNotifyHandler> serviceHandler = handlerMap.computeIfAbsent(supplierCode, k -> new HashMap<>());
                serviceHandler.putIfAbsent(serviceCode, supplierHandler);
            });
        }
        this.handlerMap = handlerMap;
    }

    /**
     * 获取供应商接口
     * @param supplierCode
     * @param serviceCode
     * @return
     */
    public PayNotifyHandler getSupplierHandler(SupplierCode supplierCode,ServiceCode serviceCode){
        Map<ServiceCode, PayNotifyHandler> payNotifyHandlerMap = handlerMap.get(supplierCode);
        if(payNotifyHandlerMap == null){
            throw new ServiceException("不支持的支付供应商");
        }
        PayNotifyHandler payNotifyHandler;
        payNotifyHandler = payNotifyHandlerMap.get(serviceCode);
        if(payNotifyHandler == null){
            throw new ServiceException("不支持的支付供应商");
        }
        return payNotifyHandler;
    }

}
