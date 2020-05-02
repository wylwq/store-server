package com.ly.storeserver.admin.models.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description:
 * @Author ly
 * @Date 2020/4/12 14:24
 * @Version V1.0.0
 **/
@Data
public class StoreRequest {

    @ApiModelProperty(value = "主键id")
    private Integer id;

    @ApiModelProperty(value = "所属一级分类名称")
    private String parentName;

    @ApiModelProperty(value = "所属二级分类名称")
    private String childrenName;

    @ApiModelProperty(value = "商品名称")
    @NotNull
    private String storeName;

    @ApiModelProperty(value = "商品库存")
    @NotNull
    private Integer storeNum;

    @ApiModelProperty(value = "商品状态")
    @NotNull
    private Integer storeStatus;

    @ApiModelProperty(value = "商品单价(单位:分)")
    @NotNull
    private Long storeFee;

    @ApiModelProperty(value = "商品属性")
    private String storeParam;

}
