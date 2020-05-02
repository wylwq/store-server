package com.ly.storeserver.admin.models.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description:
 * @Author ly
 * @Date 2020/4/3 22:56
 * @Version V1.0.0
 **/
@Data
@ApiModel(value = "用户注册实体")
public class RegisterRequest {

    @ApiModelProperty(value = "手机号码")
    @NotNull(message = "手机号不能为空~")
    private String userMobile;

    @ApiModelProperty(value = "用户密码")
    @NotNull(message = "用户密码不能为空~")
    private String password;

    @ApiModelProperty(value = "确认密码")
    @NotNull(message = "确认密码不能为空~")
    private String confirmPassword;

    @ApiModelProperty(value = "验证码")
    @NotNull(message = "验证码不能为空~")
    private String code;

}
