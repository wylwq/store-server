package com.ly.storeserver.admin.models.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author ly
 * @since 2020-05-06 22:30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("s_custom")
@ApiModel(value="Custom对象", description="")
public class Custom implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

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

    @ApiModelProperty(value = "逻辑删除字段1删除0正常")
    @TableLogic
    private Integer deleted;

    @ApiModelProperty(value = "用户版本号")
    @Version
    private Integer version;


}
