package com.ly.storeserver.common.enums;

import lombok.Getter;

/**
 * @Description:
 * @Author ly
 * @Date 2020/4/27 22:29
 * @Version V1.0.0
 **/
@Getter
public enum PayEnums {

    NO_PAY(1, "未支付"),
    PAY_SUCCESS(2, "已支付"),
    PAY_FAIL(3, "支付失败"),
    REFUNDING(4, "退款中"),
    REFUND_SUCCESS(5, "退款完成"),
    REFUND_FAIL(6, "退款失败");

    /**
     * 支付状态
     */
    private Integer status;

    /**
     * 支付描述
     */
    private String description;

    PayEnums(Integer status, String description) {
        this.status = status;
        this.description = description;
    }
}
