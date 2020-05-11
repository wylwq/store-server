package com.ly.storeserver.admin.models.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description:
 * @Author ly
 * @Date 2020/5/6 22:49
 * @Version V1.0.0
 **/
@Data
public class CustomQueryRequest extends BaseQueryRequest {

    @ApiModelProperty(value = "客户电话")
    private String cusPhone;

    @ApiModelProperty(value = "客户名称")
    private String cusName;

    @ApiModelProperty(value = "等级")
    private Integer cusMembers;
}
