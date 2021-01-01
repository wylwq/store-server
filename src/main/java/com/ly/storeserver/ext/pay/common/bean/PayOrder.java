package com.ly.storeserver.ext.pay.common.bean;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付对象
 *
 * @author : wangyu
 * @since :  2020/12/27/027 21:51
 */
@Data
public class PayOrder {

    /**
     * 商品名称
     */
    private String subject;

    /**
     * 商品描述
     */
    private String body;

    /**
     * 附加信息
     */
    private String addition;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 支付平台订单号，交易号
     */
    private String tradenNo;

    /**
     * 商户订单号
     */
    private String outTradeNo;

    /**
     * 银行卡类型
     */
    private String backType;

    /**
     * 设备信息
     */
    private String deviceInfo;

    /**
     * 支付创建ip
     */
    private String spbillCreateIp;

    /**
     * 付款条码串，人脸凭证，有关支付代码相关的
     */
    private String authCode;

    /**
     * 微信专用
     * WAP支付链接
     */
    private String wapUrl;

    /**
     * 微信专用
     * WAP支付网页名称
     */
    private String wapName;

    /**
     * 用唯一标识
     * 微信含 sub_openid字段
     * 支付宝 buyer_id
     */
    private String openid;

    /**
     * 交易类型
     */
    private TransactionType transactionType;

    /**
     * 支付币种
     */
    private CurType curType;

    /**
     * 订单过期时间
     */
    private Date expirationTime;

    public PayOrder() {
    }


    public PayOrder(String subject, String body, BigDecimal price, String outTradeNo) {
        this(subject, body, price, outTradeNo, null);
    }

    public PayOrder(String subject, String body, BigDecimal price, String outTradeNo, TransactionType transactionType) {
        this.subject = StringUtils.strip(subject);
        this.body = StringUtils.strip(body);
        this.price = price;
        this.outTradeNo = StringUtils.strip(outTradeNo);
        this.transactionType = transactionType;
    }

}
