package com.ly.storeserver.ext.open.response;

import com.ly.storeserver.ext.open.exception.ProxyApiException;
import lombok.Data;

/**
 * 基础响应类
 *
 * @author : wangyu
 * @since :  2020/12/25/025 11:19
 */
@Data
public class BaseResponse {

    /**
     * 默认响应码
     */
    private String code = ReturnCode.SUCCESS.getReturnCode();

    /**
     * 默认响应消息
     */
    private String message = ReturnCode.SUCCESS.getErrorMessage();

    private String body;

    public BaseResponse(String data) {
        this.body = data;
    }

    public BaseResponse(String code, String message, String data) {
        this.code = code;
        this.message = message;
        this.body = data;
    }

    public BaseResponse() {
        this.body = "";
    }

    public BaseResponse(ReturnCode returnCode) {
        this.code = returnCode.getReturnCode();
        this.message =returnCode.getErrorMessage();
        this.body = "";
    }

    public BaseResponse(ProxyApiException e) {
        this.code = e.getCode();
        this.message = e.getMessage();
    }
}
