package com.ly.storeserver.ext.pay.common.bean;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 退款对象
 *
 * @author : wangyu
 * @since :  2020/12/27/027 22:11
 */
@Data
public class RefundOrder {

    /**
     * 退款单号，每次进行退款的单号，唯一
     */
    private String refundNo;

    /**
     * 支付平台订单号
     */
    private String tradeNo;

    /**
     * 商户订单号
     */
    private String outTradeNo;

    /**
     * 退款金额
     */
    private BigDecimal refundAmount;

    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;

    /**
     * 退款交易日期
     */
    private Date orderDate;

    /**
     * 货币类型
     */
    private CurType curType;

    /**
     * 退款说明
     */
    private String description;

    /**
     * 退款用户
     */
    private String userId;
}
