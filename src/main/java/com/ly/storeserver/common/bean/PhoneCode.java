package com.ly.storeserver.common.bean;

import io.swagger.models.auth.In;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Description:
 * @Author ly
 * @Date 2020/4/3 15:00
 * @Version V1.0.0
 **/
@Data
public class PhoneCode {

    /**
     * 手机号
     */
    private String code;

    /**
     * 当前时间
     */
    private LocalDateTime dateTime;

    /**
     * 请求次数限制 3条/h
     */
    private Integer limitCount;
}
