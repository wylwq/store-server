package com.ly.storeserver.admin.models.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Description:
 * @Author ly
 * @Date 2020/4/12 14:31
 * @Version V1.0.0
 **/
@Data
public class StoreResponse {

    @ApiModelProperty(value = "商品主键")
    private Integer id;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "商品名称")
    private String storeName;

    @ApiModelProperty(value = "商品库存")
    private Integer storeNum;

    @ApiModelProperty(value = "商品状态")
    private Integer storeStatus;

    @ApiModelProperty(value = "商品单价(单位:分)")
    private Long storeFee;

    @ApiModelProperty(value = "商品属性")
    private String storeParam;

}
