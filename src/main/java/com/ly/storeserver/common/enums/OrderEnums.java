package com.ly.storeserver.common.enums;

import lombok.Getter;

/**
 * @Description:
 * @Author ly
 * @Date 2020/4/27 22:23
 * @Version V1.0.0
 **/
@Getter
public enum OrderEnums {

    NO_SEND(8, "未发货"),
    SEND(2, "已发货"),
    STORE(3, "未出库"),
    STORE_GO(4, "已出库"),
    ARRIVE(5, "已送达"),
    WRITER(6, "已签收"),
    CANCEL(7, "已取消"),
    DELETE(1, "已删除");

    /**
     * 订单状态
     */
    private Integer status;

    /**
     * 订单描述
     */
    private String description;

    OrderEnums(Integer status, String description) {
        this.status = status;
        this.description = description;
    }

}