package com.ly.storeserver.admin.models.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Author ly
 * @Date 2020/4/26 21:58
 * @Version V1.0.0
 **/
@Data
public class OrderRequest {

    @ApiModelProperty(value = "订单支付总金额")
    private Long orderAmount;

    @ApiModelProperty(value = "买家联系电话")
    private String userPhone;

    @ApiModelProperty(value = "支付方式")
    private Integer payType;

    @ApiModelProperty(value = "下单人")
    private String operation;

    @ApiModelProperty(value = "用户收货地址")
    private String orderAddress;

    @ApiModelProperty(value = "购物车列表")
    private List<Cart> cartList;
}
