package com.ly.storeserver.ext.open.exception;

import com.ly.storeserver.ext.open.response.ReturnCode;
import lombok.Data;

/**
 * open-api异常类
 *
 * @author : wangyu
 * @since :  2020/12/25/025 11:13
 */
@Data
public class ProxyApiException extends RuntimeException {

    private String code;

    private String message;

    public ProxyApiException(ReturnCode data) {
        super(data.getErrorMessage());
        this.code = data.getReturnCode();
        this.message = data.getErrorMessage();
    }

    public ProxyApiException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

}
