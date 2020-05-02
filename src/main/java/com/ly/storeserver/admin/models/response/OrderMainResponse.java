package com.ly.storeserver.admin.models.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Description:
 * @Author ly
 * @Date 2020/4/27 22:00
 * @Version V1.0.0
 **/
@Data
public class OrderMainResponse {

    @ApiModelProperty(value = "订单id")
    private Long id;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "订单状态")
    private Integer orderStatus;

    @ApiModelProperty(value = "订单支付总金额")
    private Long orderAmount;

    @ApiModelProperty(value = "买家联系电话")
    private String userPhone;

    @ApiModelProperty(value = "支付方式")
    private Integer payType;

    @ApiModelProperty(value = "支付状态")
    private Integer payStatus;

    @ApiModelProperty(value = "用户收货地址")
    private String orderAddress;

    @ApiModelProperty(value = "下单日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}
