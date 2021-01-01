package com.ly.storeserver.ext.pay.common.handle;

/**
 * 支付配置信息
 *
 * @author : wangyu
 * @since :  2020/12/27/027 20:51
 */
public interface PayConfigStorage {

    /**
     * 应用id
     *
     * @return 应用id
     */
    String getAppid();

    /**
     * 合作商唯一标识
     *
     * @return 合作商唯一标识
     */
    String getPid();

    /**
     * 获取收款账号
     *
     * @return 收款账号
     */
    String getSeller();

    /**
     * 服务端异步回调Url
     *
     * @return 异步回调Url
     */
    String getNotifyUrl();

    /**
     * 服务端同步Url
     *
     * @return 同步回调Url
     */
    String getReturnUrl();

    /**
     * 签名方式
     *
     * @return 签名方式
     */
    String getSignType();

    /**
     * 字符编码格式
     *
     * @return 字符编码
     */
    String getInputCharset();

    /**
     * 支付平台公钥(签名校验使用)
     *
     * @return 公钥
     */
    String getKeyPublic();

    /**
     * 应用私钥(生成签名时使用)
     *
     * @return 私钥
     */
    String getKeyPrivate();

    /**
     * 支付类型 自定义
     * 例如：aliPay 支付宝，wxPay微信支付
     *
     * @return 支付类型
     */
    String getPayType();

    /**
     * 是否为测试环境
     *
     * @return true测试环境
     */
    boolean isTest();


}
