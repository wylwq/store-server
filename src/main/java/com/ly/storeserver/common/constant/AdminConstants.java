package com.ly.storeserver.common.constant;

/**
 * @Description: 管理端常量
 * @Author ly
 * @Date 2020/4/3 15:10
 * @Version V1.0.0
 **/
public interface AdminConstants {

    /**
     * 手机验证码的有效时间是10分钟
     */
    Integer CODE_EXPIRES = 600;

    /**
     * 手机验证码的有效时间是一小时
     */
    Integer PHONECODE_EXPIRES = 3600;

    /**
     * 验证码发送次数限制3次/h
     */
    Integer CODE_LIMIT_HOUR = 3;

    /**
     * 6位默认验证码
     */
    String DEFAULT_CODE = "666666";

}
