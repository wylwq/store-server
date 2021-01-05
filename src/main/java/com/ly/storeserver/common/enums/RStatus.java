package com.ly.storeserver.common.enums;

import lombok.Getter;

/**
 * @Description:
 * @Author ly
 * @Date 2020/4/3 10:24
 * @Version V1.0.0
 **/
@Getter
public enum RStatus {

    SUCCESS(1001, "成功"),
    AUTH_EXPIRES(1002, "授权过期"),
    FAIL(1005, "失败"),
    NO_LOGIN(1006, "没有登录"),
    NO_FERMISSION(1007, "没有权限"),
    SERVER_ERROR(2001, "服务器内部错误"),
    PARAMS_ERROR(2002, "参数校验错误"),
    SEDN_CODE(2003, "验证码频率3次/小时"),
    REQ_LIMIT(2004, "请求频率过快，请稍后再试~"),
    ;

    private Integer code;

    private String message;

    RStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
