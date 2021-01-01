package com.ly.storeserver.ext.pay.common.util.sign.encrypt;

import cn.hutool.crypto.digest.DigestUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * md5签名工具类
 *
 * @author : wangyu
 * @since :  2020/12/28/028 10:28
 */
public class MD5 {

    /**
     * 签名字符串
     *
     * @param text 需要签名的字符串
     * @param key 密钥
     * @param inputCharset 编码格式
     * @return 结果
     */
    public static String sign(String text, String key, String inputCharset){
        //拼接key
        text = text + key;
        return DigestUtil.md5Hex(text);
    }

    /**
     * 验证签名是否正确
     *
     * @param text 需要签名的字符串
     * @param sign 签名结果
     * @param key  密钥
     * @param inputCharacter 编码格式
     * @return 验签结果
     */
    public static boolean verify(String text, String sign, String key, String inputCharacter){
        //判断是否一样
        return StringUtils.equals(sign(text, key, inputCharacter).toUpperCase(), sign.toUpperCase());
    }

}
