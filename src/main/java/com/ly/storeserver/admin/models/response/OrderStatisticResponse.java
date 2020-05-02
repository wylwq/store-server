package com.ly.storeserver.admin.models.response;

import lombok.Data;

/**
 * @Description:
 * @Author ly
 * @Date 2020/4/30 20:46
 * @Version V1.0.0
 **/
@Data
public class OrderStatisticResponse {

    /**
     * 日期 yyyy-MM-dd
     */
    private String date;

    /**
     * 销量
     */
    private Long num;
}
