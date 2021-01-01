package com.ly.storeserver.ext.pay.common.constant;

import lombok.Getter;

/**
 * 具体回调实体
 *
 * @author : wangyu
 * @since :  2020/12/31/031 16:08
 */
@Getter
public enum ServiceCode {

    PAY_NOTIFY("pay_notify", "支付回调"),
    REFUND_NOTIFY("refund_notify", "退款回调")
    ;

    /**
     * 回调编码
     */
    private String code;

    /**
     * 回调名称
     */
    private String name;

    ServiceCode(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static ServiceCode getByCode(String code) {
        for (ServiceCode serviceCode : values()) {
            if (serviceCode.code.endsWith(code)) {
                return serviceCode;
            }
        }
        return null;
    }
}
