package com.ly.storeserver.ext.pay.common.handle;

import cn.hutool.extra.qrcode.QrCodeUtil;
import com.google.zxing.BarcodeFormat;
import com.ly.storeserver.ext.pay.common.bean.MethodType;
import com.ly.storeserver.ext.pay.common.bean.PayOrder;
import com.ly.storeserver.ext.pay.common.http.HttpConfigStorage;
import com.ly.storeserver.ext.pay.common.http.HttpRequestTemplate;
import com.ly.storeserver.ext.pay.common.util.sign.SignUtils;
import lombok.extern.slf4j.Slf4j;

import java.awt.image.BufferedImage;
import java.util.Map;

/**
 * 抽象的支付处理器
 *
 * @author : wangyu
 * @since :  2020/12/28/028 9:55
 */
@Slf4j
public abstract class BasePayHandler<PC extends PayConfigStorage> implements PayHandler<PC> {

    protected PC payConfigStorage;

    protected HttpRequestTemplate requestTemplate;

    protected int retrySleepMillis = 1000;

    protected int maxRetryTimes = 5;

    /**
     * 设置支付配置
     *
     * @param payConfigStorage 支付配置
     * @return
     */
    @Override
    public BasePayHandler setPayConfigStorage(PC payConfigStorage) {
        this.payConfigStorage = payConfigStorage;
        return this;
    }

    @Override
    public PC getConfigStorage(){
        return payConfigStorage;
    }

    @Override
    public HttpRequestTemplate getHttpRequestTemplate(){
        return requestTemplate;
    }

    /**
     * 设置并创建请求模板，代理请求配置
     *
     * @param configStorage http请求配置参数
     * @return
     */
    @Override
    public BasePayHandler setHttpRequestTemplateConfigStorage(HttpConfigStorage configStorage) {
        this.requestTemplate = new HttpRequestTemplate();
        return this;
    }

    public BasePayHandler(PC payConfigStorage) {
        this(payConfigStorage, null);
    }

    public BasePayHandler(PC payConfigStorage, HttpConfigStorage configStorage){
        setPayConfigStorage(payConfigStorage);
        setHttpRequestTemplateConfigStorage(configStorage);
    }

    /**
     * 创建签名
     *
     * @param content 需要签名的内容
     * @param characterEncoding 字符编码
     * @return
     */
    @Override
    public String createSign(String content, String characterEncoding) {

        return SignUtils.valueOf(payConfigStorage.getSignType()).createSign(content, payConfigStorage.getKeyPrivate(), characterEncoding);
    }

    /**
     * 页面转跳支付， 返回对应页面重定向信息
     *
     * @param order 订单信息
     * @return 对应页面重定向信息
     */
    @Override
    public <O extends PayOrder> String toPay(O order) {
        //这里进行对应的业务逻辑代码处理
        Map<String, Object> orderInfo = orderInfo(order);
        return buildRequest(orderInfo, MethodType.POST);
    }

    /**
     *
     * @param order  订单信息
     * @param <O> 预订单类型
     * @return 对应app所需参数信息
     */
    @Override
    public <O extends PayOrder> Map<String, Object> app(O order) {
        return orderInfo(order);
    }

    /**
     * 生成支付二维码
     *
     * @param order 发起支付的订单信息
     * @return 返回图片信息，支付时需要
     */
    @Override
    public <O extends PayOrder> BufferedImage genQrPay(O order) {
        String context = getQrPay(order);
        return QrCodeUtil.generate(context, BarcodeFormat.QR_CODE, 250, 250);
    }
}
