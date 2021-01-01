package com.ly.storeserver.ext.pay.common.handle;

import com.ly.storeserver.ext.pay.common.constant.ServiceCode;
import com.ly.storeserver.ext.pay.common.constant.SupplierCode;

import java.util.Map;

/**
 * 支付回调通知接口
 *
 * @author : wangyu
 * @since :  2020/12/31/031 16:14
 */
public interface PayNotifyHandler {

    /**
     * 供应商
     *
     * @return
     */
    SupplierCode supplier();

    /**
     * 服务
     *
     * @return
     */
    ServiceCode service();

    /**
     * 处理对象
     *
     * @param requestBody
     * @return
     */
    String handler(Map<String, String> requestBody);

}
