package com.ly.storeserver.common.enums;

import lombok.Getter;

/**
 * @Description:
 * @Author ly
 * @Date 2020/4/27 22:35
 * @Version V1.0.0
 **/
@Getter
public enum PayTypeEnums {

    WEIXIN(1, "微信支付"),
    ZHIFUBAO(2, "支付宝支付"),
    XIANJING(3, "货到付款"),
    FENQI(4, "分期付款"),
    JIFEN(5, "积分支付");

    /**
     * 支付方式
     */
    private Integer type;

    /**
     * 支付描述
     */
    private String description;

    PayTypeEnums(Integer type, String description) {
        this.type = type;
        this.description = description;
    }
}
