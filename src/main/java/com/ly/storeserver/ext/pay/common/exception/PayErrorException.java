package com.ly.storeserver.ext.pay.common.exception;

/**
 * 支付统一异常
 *
 * @author : wangyu
 * @since :  2020/12/28/028 11:14
 */
public class PayErrorException extends RuntimeException {

    private PayError payError;

    public PayErrorException(PayError payError) {
        super(payError.getErrorMsg());
        this.payError = payError;
    }

    public PayError getPayError() {
        return payError;
    }
}
