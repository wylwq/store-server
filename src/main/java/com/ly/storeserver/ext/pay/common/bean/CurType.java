package com.ly.storeserver.ext.pay.common.bean;

/**
 * 支付币种
 *
 * @author : wangyu
 * @since :  2020/12/27/027 22:05
 */
public interface CurType {

    /**
     * 获取货币类型
     * @return 货币类型
     */
    String getType();

    /**
     * 货币名称
     * @return 货币名称
     */
    String getName();
}
