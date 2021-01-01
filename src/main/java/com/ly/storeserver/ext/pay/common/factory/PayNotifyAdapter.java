package com.ly.storeserver.ext.pay.common.factory;

import com.ly.storeserver.ext.pay.common.constant.ServiceCode;
import com.ly.storeserver.ext.pay.common.constant.SupplierCode;
import com.ly.storeserver.ext.pay.common.handle.PayNotifyHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 统一适配器
 *
 * @author : wangyu
 * @since :  2020/12/31/031 16:06
 */
@Service
@Slf4j
public class PayNotifyAdapter {

    @Autowired
    private PayNotifyFactory payNotifyFactory;

    /**
     * 适配器方法，将具体的服务转发到具体的handler处理
     *
     * @param requestBody 响应实体
     * @param supplierCode 供应商
     * @param type 服务方法
     * @return
     */
    public String handler(Map<String, String> requestBody, SupplierCode supplierCode, ServiceCode type) {
        //实例代码，不包含业务代码
        PayNotifyHandler supplierHandler = payNotifyFactory.getSupplierHandler(supplierCode, type);
        return supplierHandler.handler(requestBody);
    }
}
