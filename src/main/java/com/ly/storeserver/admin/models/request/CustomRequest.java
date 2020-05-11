package com.ly.storeserver.admin.models.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description:
 * @Author ly
 * @Date 2020/5/6 22:48
 * @Version V1.0.0
 **/
@Data
public class CustomRequest {

    @ApiModelProperty(value = "用户id")
    private Long id;

    @ApiModelProperty(value = "客户电话")
    private String cusPhone;

    @ApiModelProperty(value = "客户名称")
    private String cusName;

    @ApiModelProperty(value = "折扣")
    private Float cusDiscount;

    @ApiModelProperty(value = "等级")
    private Integer cusMembers;

    @ApiModelProperty(value = "用户地址")
    private String cusAddress;

}
