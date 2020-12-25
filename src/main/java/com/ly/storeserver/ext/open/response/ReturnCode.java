package com.ly.storeserver.ext.open.response;

import lombok.Getter;

/**
 * 响应状态码
 *
 * @author : wangyu
 * @since :  2020/12/25/025 11:16
 */
@Getter
public enum ReturnCode {

    SUCCESS("1000", "成功"),
    DECODE_FAIL("1001", "appSecert校验失败"),
    AUTH_FAIL("1002", "权限验证失败"),
    IP_FORBIDDEN ("1003", "IP准入验证失败"),
    BAD_REQUEST("1004", "编码不符合要求"),
    DUPLICATED_DATA("1005", "数据重复"),
    DATA_TOO_LONG("1006", "同步数据量过大"),
    INTERNAL_SERVER_ERROR("1901", "其他失败类型"),
    RATE_FORBIDDEN("1007", "请求频率超限制"),
    PARAM_NULL("1008", "参数为空"),
        ;

    /**
     * 响应码
     */
    private String returnCode;

    /**
     * 提示消息
     */
    private String errorMessage;

    ReturnCode(String returnCode, String errorMessage) {
        this.returnCode = returnCode;
        this.errorMessage = errorMessage;
    }

}
