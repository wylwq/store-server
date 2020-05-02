package com.ly.storeserver.admin.models.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Description:
 * @Author ly
 * @Date 2020/4/9 7:20
 * @Version V1.0.0
 **/
@Data
@ApiModel(value = "服装分类响应实体")
public class CategoryResponse {

    @ApiModelProperty(value = "分类id")
    private Integer id;

    @ApiModelProperty(value = "分类名称")
    private String categoryName;

    @ApiModelProperty(value = "父级分类id")
    private Integer parentId;

    @ApiModelProperty(value = "操作人")
    private String operation;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;


}
