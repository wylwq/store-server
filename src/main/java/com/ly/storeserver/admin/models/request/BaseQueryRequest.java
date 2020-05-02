package com.ly.storeserver.admin.models.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description:
 * @Author ly
 * @Date 2020/4/24 7:36
 * @Version V1.0.0
 **/
@Data
public class BaseQueryRequest {

    @ApiModelProperty(value = "当前页码")
    private Integer pageNumber;

    @ApiModelProperty(value = "每页大小")
    private Integer pageSize;

}
