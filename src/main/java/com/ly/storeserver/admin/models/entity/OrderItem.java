package com.ly.storeserver.admin.models.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ly
 * @since 2020-04-26 21:46
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("s_order_item")
@ApiModel(value="OrderItem对象", description="")
public class OrderItem implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "订单项id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "订单项号")
    private String orderItemNo;

    @ApiModelProperty(value = "主订单编号")
    private Long orderMainId;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品id")
    private Long goodsId;

    @ApiModelProperty(value = "商品单价")
    private Long goodsPrice;

    @ApiModelProperty(value = "预定数量")
    private Integer goodsNum;

    @ApiModelProperty(value = "子订单状态")
    private Integer orderItemStatus;

    @ApiModelProperty(value = "子订单支付总金额")
    private Long goodsTotal;

    @ApiModelProperty(value = "逻辑删除字段1删除0正常")
    @TableLogic
    private Integer deleted;

    @ApiModelProperty(value = "版本号")
    @Version
    private Integer version;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


}
