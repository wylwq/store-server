package com.ly.storeserver.admin.models.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description:
 * @Author ly
 * @Date 2020/4/23 23:40
 * @Version V1.0.0
 **/
@Data
public class UserQueryRequest extends BaseQueryRequest{

    @ApiModelProperty(value = "用户名称")
    private String userName;

    @ApiModelProperty(value = "用户手机号")
    private String userPhone;

    @ApiModelProperty(value = "住址")
    private String address;

    @ApiModelProperty(value = "用户性别")
    private String sex;

}
