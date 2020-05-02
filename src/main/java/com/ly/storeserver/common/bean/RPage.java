package com.ly.storeserver.common.bean;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ly.storeserver.common.enums.RStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description:
 * @Author ly
 * @Date 2020/4/3 11:09
 * @Version V1.0.0
 **/
@ApiModel("分页数据返回对象")
@Data
public class RPage<T> implements Serializable {

    private static final long serialVersionUID = -5171037347725994494L;


    @ApiModelProperty("消息")
    private String msg = RStatus.SUCCESS.getMessage();


    @ApiModelProperty("响应状态码")
    private int code = RStatus.SUCCESS.getCode();


    @ApiModelProperty("不分页情况下数据总数")
    private Long total;


    @ApiModelProperty("查询条数")
    private Long size;


    @ApiModelProperty("页数")
    private Long current;


    @ApiModelProperty("是否存在上一页")
    private Boolean hasPrevious;


    @ApiModelProperty("是否存在下一页")
    private Boolean hasNext;


    @ApiModelProperty("分页数据")
    private List<T> data;

    public RPage(){}

    public RPage(IPage<T> page) {
        if (page == null) {
            return;
        }
        this.total = page.getTotal();
        this.current = page.getCurrent();
        this.size = page.getSize();
        this.hasPrevious = hasPrevious(page);
        this.hasNext = hasNext(page);
        this.data = page.getRecords();
    }

    public RPage<T> msg(String msg) {
        this.msg = msg;
        return this;
    }

    public RPage<T> code(int code) {
        this.code = code;
        return this;
    }

    /**
     * <p>
     * 是否存在上一页
     * </p>
     *
     * @return true / false
     */
    private boolean hasPrevious(IPage page) {
        return page.getCurrent() > 1;
    }

    /**
     * <p>
     * 是否存在下一页
     * </p>
     *
     * @return true / false
     */
    private boolean hasNext(IPage page) {
        return page.getCurrent() < page.getPages();
    }
}



