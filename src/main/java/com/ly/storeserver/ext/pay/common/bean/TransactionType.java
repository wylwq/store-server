package com.ly.storeserver.ext.pay.common.bean;

/**
 * 交易类型
 *
 * @author : wangyu
 * @since :  2020/12/27/027 22:06
 */
public interface TransactionType {

    /**
     * 获取交易类型
     * @return 接口
     */
    String getType();

    /**
     * 获取接口
     * @return 接口
     */
    String getMethod();
}
