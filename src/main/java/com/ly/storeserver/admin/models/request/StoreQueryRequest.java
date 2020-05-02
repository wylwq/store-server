package com.ly.storeserver.admin.models.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;

/**
 * @Description:
 * @Author ly
 * @Date 2020/4/12 14:28
 * @Version V1.0.0
 **/
@Data
public class StoreQueryRequest extends BaseQueryRequest{

    @ApiModelProperty(value = "类型名称")
    private String storeParent;

    @ApiModelProperty(value = "商品状态")
    private Integer storeStatus;

    @ApiModelProperty(value = "商品创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "商品名称")
    private String keyWord;

}
