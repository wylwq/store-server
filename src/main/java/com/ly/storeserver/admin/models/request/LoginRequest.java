package com.ly.storeserver.admin.models.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @Description:
 * @Author ly
 * @Date 2020/4/3 22:15
 * @Version V1.0.0
 **/
@Data
@ApiModel(value = "登录请求实体")
public class LoginRequest {

    @ApiModelProperty(value = "手机号码")
    @NotEmpty(message = "用户名不能为空~")
    private String userMobile;

    @ApiModelProperty(value = "用户密码")
    @NotEmpty(message = "用户密码不能为空~")
    private String password;

}
