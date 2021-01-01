package com.ly.storeserver.ext.pay.common.exception;

/**
 * 支付异常类
 *
 * @author : wangyu
 * @since :  2020/12/28/028 11:18
 */
public class PayException implements PayError {

    private String errorCode;

    private String errorMsg;

    private String content;

    public PayException(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public PayException(String errorCode, String errorMsg, String content) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.content = content;
    }

    /**
     * 获取错误码
     *
     * @return 返回错误码
     */
    @Override
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * 获取错误消息
     *
     * @return 返回错误信息
     */
    @Override
    public String getErrorMsg() {
        return errorMsg;
    }

    @Override
    public String getString() {
        return "支付错误: errcode=" + errorCode + ", errmsg=" + errorMsg + (null == content ? "" : "\n content:" + content);
    }

}
