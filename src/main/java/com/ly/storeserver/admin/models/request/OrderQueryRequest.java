package com.ly.storeserver.admin.models.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description:
 * @Author ly
 * @Date 2020/4/26 22:08
 * @Version V1.0.0
 **/
@Data
public class OrderQueryRequest extends BaseQueryRequest{

    @ApiModelProperty(value = "主订单编号")
    private String orderNo;

    @ApiModelProperty(value = "订单创建时间")
    private String createTime;

    @ApiModelProperty(value = "订单状态")
    private Integer orderStatus;

}
