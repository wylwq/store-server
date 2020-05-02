package com.ly.storeserver.admin.models.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description:
 * @Author ly
 * @Date 2020/4/27 21:56
 * @Version V1.0.0
 **/
@Data
public class Cart {

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品id")
    private Long goodsId;

    @ApiModelProperty(value = "商品单价")
    private Long goodsPrice;

    @ApiModelProperty(value = "预定数量")
    private Integer goodsNum;

    @ApiModelProperty(value = "子订单支付总金额")
    private Long goodsTotal;
}
