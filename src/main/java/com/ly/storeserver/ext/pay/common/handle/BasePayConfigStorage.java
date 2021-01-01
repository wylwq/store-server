package com.ly.storeserver.ext.pay.common.handle;

import lombok.Data;

/**
 * 支付配置抽象类
 *
 * @author : wangyu
 * @since :  2020/12/27/027 21:43
 */
@Data
public abstract class BasePayConfigStorage implements PayConfigStorage{

    /**
     * 应用私钥，ras_private pkcs8格式 生成签名时使用
     */
    private String keyPrivate;

    /**
     * 支付平台公钥（签名校验使用）
     */
    private String keyPublic;

    /**
     * 异步回调地址
     */
    private String notifyUrl;

    /**
     * 同步回调地址,支付完成后展示的页面
     */
    private String returnUrl;

    /**
     * 签名加密类型
     */
    private String signType;

    /**
     * 字符类型
     */
    private String inputCharset;

    /**
     * 支付类型 aliPay支付宝, wxPay微信等
     */
    private String payType;

    /**
     * 是否为沙箱环境，默认为正式环境
     */
    private boolean isTest = false;

    /**
     * 是否为证书签名
     */
    private boolean certSign = false;

    @Override
    public boolean isTest() {
        return isTest;
    }

    public void setTest(boolean test) {
        isTest = test;
    }

}
