package com.ly.storeserver.ext.pay.common.exception;

/**
 * 支付异常信息
 *
 * @author : wangyu
 * @since :  2020/12/28/028 11:15
 */
public interface PayError {

    /**
     * 获取错误码
     *
     * @return 返回错误码
     */
    String getErrorCode();

    /**
     * 获取错误消息
     *
     * @return 返回错误信息
     */
    String getErrorMsg();

    /**
     * 错误信息
     *
     * @return
     */
    String getString();
}
