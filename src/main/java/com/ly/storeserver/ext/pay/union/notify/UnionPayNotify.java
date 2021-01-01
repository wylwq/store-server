package com.ly.storeserver.ext.pay.union.notify;

import com.alibaba.fastjson.JSON;
import com.ly.storeserver.ext.pay.common.constant.ServiceCode;
import com.ly.storeserver.ext.pay.common.constant.SupplierCode;
import com.ly.storeserver.ext.pay.common.handle.AbstractPayNotifyHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 银联支付回调接口
 *
 * @author : wangyu
 * @since :  2020/12/31/031 16:35
 */
@Service
@Slf4j
public class UnionPayNotify extends AbstractPayNotifyHandler {
    /**
     * 供应商
     *
     * @return
     */
    @Override
    public SupplierCode supplier() {
        return SupplierCode.UNION;
    }

    /**
     * 服务
     *
     * @return
     */
    @Override
    public ServiceCode service() {
        return ServiceCode.PAY_NOTIFY;
    }

    /**
     * 处理对象
     *
     * @param requestBody 远程调用请求参数
     * @return
     */
    @Override
    public String handler(Map<String, String> requestBody) {
        //签名验证
        String respCode = requestBody.get("respCode");
        log.info("订单状态：{}", respCode);
        if ("00".equals(respCode)) {
            //业务处理
            String orderId = requestBody.get("orderId");
            log.info("订单id：{}", orderId);
        }
        return createResp();
    }

    private String createResp() {
        //返回给银联服务器http 200  状态码
        //resp.getWriter().print("ok");
        return "ok";
    }
}
