package com.ly.storeserver.admin.models.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Description:
 * @Author ly
 * @Date 2020/5/6 22:51
 * @Version V1.0.0
 **/
@Data
public class CustomResponse {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "客户电话")
    private String cusPhone;

    @ApiModelProperty(value = "客户名称")
    private String cusName;

    @ApiModelProperty(value = "折扣")
    private Integer cusDiscount;

    @ApiModelProperty(value = "等级")
    private Integer cusMembers;

    @ApiModelProperty(value = "用户地址")
    private String cusAddress;


}
