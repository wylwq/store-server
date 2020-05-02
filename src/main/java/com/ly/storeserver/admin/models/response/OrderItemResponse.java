package com.ly.storeserver.admin.models.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description:
 * @Author ly
 * @Date 2020/4/27 22:01
 * @Version V1.0.0
 **/
@Data
public class OrderItemResponse {

    @ApiModelProperty(value = "订单项号")
    private String orderItemNo;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品单价")
    private Long goodsPrice;

    @ApiModelProperty(value = "预定数量")
    private Integer goodsNum;

    @ApiModelProperty(value = "子订单状态")
    private Integer orderItemStatus;

    @ApiModelProperty(value = "子订单支付总金额")
    private Long goodsTotal;

    @ApiModelProperty(value = "收货地址")
    private String orderAddress;

}
