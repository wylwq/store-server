package com.ly.storeserver.utils;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;

/**
 * aes对称加密算法
 *
 * @author : wangyu
 * @since :  2020/12/25/025 15:04
 */
public class AESUtil {

    /**
     * 加密使用的key
     */
    private final static String key = "O228aK2DgfY7BXZV";


    /**
     * 加密字符串
     *
     * @param content
     * @return
     */
    public static String encrypt(String content) {
        // 构建
        AES aes = SecureUtil.aes(key.getBytes());
        return aes.encryptHex(content);
    }


    /**
     * 解密字符串
     *
     * @param encryptHex
     * @return
     */
    public static String decryptStr(String encryptHex) {
        // 构建
        AES aes = SecureUtil.aes(key.getBytes());
        return aes.decryptStr(encryptHex, CharsetUtil.CHARSET_UTF_8);
    }


    /**
     * 加密字符串
     *
     * @param content
     * @return
     */
    public static String encrypt(String key, String content) {
        // 构建
        AES aes = SecureUtil.aes(key.getBytes());
        byte[] encrypt = aes.encrypt(content);
        return Base64Encoder.encode(encrypt);
    }


    /**
     * 解密字符串
     *
     * @param encryptHex
     * @return
     */
    public static String decryptStr(String key, String encryptHex) {
        // 构建
        AES aes = SecureUtil.aes(key.getBytes());
        byte[] contentBytes = Base64Decoder.decode(encryptHex);
        byte[] decrypt = aes.decrypt(contentBytes);
        return StrUtil.str(decrypt, CharsetUtil.CHARSET_UTF_8);
    }

}
