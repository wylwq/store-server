package com.ly.storeserver.ext.pay.common.handle;

import java.util.Map;

/**
 * 支付回调处理函数,可用于类型转换
 *
 * @author : wangyu
 * @since :  2020/12/27/027 21:27
 */
public interface Callback<T> {

    /**
     * 执行者
     * @param map 需要转换的map
     * @return 处理过后的类型对象
     */
    T transForm(Map<String, Object> map);
}
