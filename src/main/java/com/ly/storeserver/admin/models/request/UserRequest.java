package com.ly.storeserver.admin.models.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @Description:
 * @Author ly
 * @Date 2020/4/23 23:29
 * @Version V1.0.0
 **/
@Data
public class UserRequest {

    @ApiModelProperty(value = "用户id")
    private Long id;

    @ApiModelProperty(value = "用户名称")
    private String userName;

    @ApiModelProperty(value = "用户手机号")
    private String userPhone;

    @ApiModelProperty(value = "用户年龄")
    private Integer userAge;

    @ApiModelProperty(value = "用户性别")
    private String userSex;

    @ApiModelProperty(value = "用户家庭住址")
    private String userAddress;

    @ApiModelProperty(value = "管理员标志")
    private String adminFlag;
}
