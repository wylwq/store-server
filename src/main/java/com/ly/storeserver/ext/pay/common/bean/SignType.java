package com.ly.storeserver.ext.pay.common.bean;

/**
 * 签名类型
 *
 * @author : wangyu
 * @since :  2020/12/28/028 10:09
 */
public interface SignType {

    /**
     * 获取签名类型名称
     * @return
     */
    String getName();

    /**
     * 签名
     *
     * @param content 需要签名的内容
     * @param key 密钥
     * @param characterEncoding 字符编码
     * @return
     */
    String createSign(String content, String key, String characterEncoding);

    /**
     * 签名字符串
     *
     * @param text              需要签名的字符串
     * @param sign              签名结果
     * @param key               密钥
     * @param characterEncoding 编码格式
     * @return 签名结果
     */
    boolean verify(String text, String sign, String key, String characterEncoding);

}
