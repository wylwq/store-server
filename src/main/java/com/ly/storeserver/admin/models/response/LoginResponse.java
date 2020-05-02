package com.ly.storeserver.admin.models.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description:
 * @Author ly
 * @Date 2020/4/3 23:14
 * @Version V1.0.0
 **/
@Data
@ApiModel(value = "登录响应实体")
public class LoginResponse {

    @ApiModelProperty(value = "token令牌")
    private String token;

    @ApiModelProperty(value = "用户名称")
    private String username;

    @ApiModelProperty(value = "是否是管理员")
    private String adminFlag;

}
