package com.ly.storeserver.admin.models.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description:
 * @Author ly
 * @Date 2020/4/9 7:31
 * @Version V1.0.0
 **/
@Data
@ApiModel(value = "分类请求实体类")
public class CategoryRequest {

    @ApiModelProperty(value = "分类id")
    @NotNull
    private Integer id;

    @ApiModelProperty(value = "一级分类名称")
    @NotNull
    private String categoryName;

}
