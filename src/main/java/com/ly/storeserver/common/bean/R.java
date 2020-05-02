package com.ly.storeserver.common.bean;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.ly.storeserver.common.enums.RStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description:
 * @Author ly
 * @Date 2020/4/2 23:50
 * @Version V1.0.0
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
public class R<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 消息
     */
    private String msg = RStatus.SUCCESS.getMessage();

    /**
     * 状态码
     */
    private Integer code = RStatus.SUCCESS.getCode();

    /**
     * 消息体
     */
    @JsonAlias(value = "datas")
    private T data;

    public R(RStatus status) {
        this.code = status.getCode();
        this.msg = status.getMessage();
    }

    public R(RStatus status, T data) {
        this.msg = status.getMessage();
        this.code = status.getCode();
        this.data = data;
    }

    public R(T data) {
        this.data = data;
    }

    public R(String message) {
        this.msg = message;
    }

    public R(String message, T data) {
        this.msg = message;
        this.data = data;
    }

    public R(Integer code, String message) {
        this.code = code;
        this.msg = message;
    }

}
