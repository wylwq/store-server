package com.ly.storeserver.ext.pay.common.handle;

import com.ly.storeserver.ext.pay.common.bean.*;
import com.ly.storeserver.ext.pay.common.http.HttpConfigStorage;
import com.ly.storeserver.ext.pay.common.http.HttpRequestTemplate;

import java.awt.image.BufferedImage;
import java.util.Map;

/**
 * 支付抽象接口
 *
 * @author : wangyu
 * @since :  2020/12/27/027 20:47
 */
public interface PayHandler<PC extends PayConfigStorage> {

    /**
     * 设置支付配置参数
     *
     * @param payConfigStorage 支付配置
     * @return 支付服务
     */
    PayHandler setPayConfigStorage(PC payConfigStorage);

    /**
     * 获取支付配置参数
     *
     * @return 支付配置
     */
    PC getConfigStorage();

    /**
     * 获取http请求工具
     *
     * @return http请求工具
     */
    HttpRequestTemplate getHttpRequestTemplate();

    /**
     * 设置请求工具配置
     *
     * @param configStorage http请求配置参数
     * @return 支付服务
     */
    PayHandler setHttpRequestTemplateConfigStorage(HttpConfigStorage configStorage);

    /**
     * 获取支付请求地址
     * @param transactionType
     * @return
     */
    String getReqUrl(TransactionType transactionType);

    /**
     * 回调参数校验   远端支付接口回调过来的参数
     *
     * @param params  签名校验 true通过
     */
    boolean verify(Map<String, Object> params);

    /**
     * 返回创建的订单信息
     *
     * @param order 支付订单
     * @param <O>   预订单类型
     * @return  支付订单信息
     */
    <O extends PayOrder> Map<String, Object> orderInfo(O order);

    /**
     * 获取输出消息，用户返回给支付端, 针对于web端
     *
     * @param orderInfo 发起支付的订单信息
     * @param method    请求方式  "post" "get",
     * @return 获取输出消息，用户返回给支付端, 针对于web端
     * @see MethodType 请求类型
     */
    String buildRequest(Map<String, Object> orderInfo, MethodType method);

    /**
     * 页面跳转支付，返回对应页面重定向信息
     *
     * @param order 订单信息
     * @param <O>   预订单类型
     * @return  对应页面重定向信息
     */
    <O extends PayOrder> String toPay(O order);

    /**
     * app支付
     *
     * @param order  订单信息
     * @param <O>  预订单类型
     * @return 对应app所需参数信息
     */
    <O extends PayOrder> Map<String, Object> app(O order);

    /**
     * 创建签名
     * @param content 需要签名的内容
     * @param characterEncoding 字符编码
     * @return 签名
     */
    String createSign(String content, String characterEncoding);

    /**
     * 获取输出二维码，用户返回给支付端,
     *
     * @param order 发起支付的订单信息
     * @param <O> 预订单类型
     * @return 返回图片信息，支付时需要的
     */
    <O extends PayOrder> BufferedImage genQrPay(O order);

    /**
     * 获取输出二维码信息,
     *
     * @param order 发起支付的订单信息
     * @param <O> 预订单类型
     * @return 返回二维码信息,，支付时需要的
     */
    <O extends PayOrder> String getQrPay(O order);

    /**
     * 交易查询接口
     *
     * @param tradeNo 支付平台订单号
     * @param outTradeNo 商户单号
     * @return
     */
    Map<String, Object> query(String tradeNo, String outTradeNo);

    /**
     * 交易查询接口，带处理器
     *
     * @param tradeNo 支付平台订单号
     * @param outTradeNo 商户单号
     * @param callback 处理器
     * @param <T> 返回类型
     * @return 返回查询回来的结果集
     */
    <T> T query(String tradeNo, String outTradeNo, Callback<T> callback);

    /**
     * 交易关闭接口
     *
     * @param tradeNo    支付平台订单号
     * @param outTradeNo 商户单号
     * @return 返回支付方交易关闭后的结果
     */
    Map<String, Object> close(String tradeNo, String outTradeNo);


    /**
     * 交易关闭接口
     *
     * @param tradeNo    支付平台订单号
     * @param outTradeNo 商户单号
     * @param callback   处理器
     * @param <T>        返回类型
     * @return 返回支付方交易关闭后的结果
     */
    <T> T close(String tradeNo, String outTradeNo, Callback<T> callback);

    /**
     * 交易交易撤销
     *
     * @param tradeNo    支付平台订单号
     * @param outTradeNo 商户单号
     * @return 返回支付方交易撤销后的结果
     */
    Map<String, Object> cancel(String tradeNo, String outTradeNo);

    /**
     * 交易交易撤销
     *
     * @param tradeNo    支付平台订单号
     * @param outTradeNo 商户单号
     * @param callback   处理器
     * @param <T>        返回类型
     * @return 返回支付方交易撤销后的结果
     */
    <T> T cancel(String tradeNo, String outTradeNo, Callback<T> callback);

    /**
     * 申请退款接口
     *
     * @param refundOrder 退款订单信息
     * @return 返回支付方申请退款后的结果
     */
    RefundResult refund(RefundOrder refundOrder);

    /**
     * 申请退款接口
     *
     * @param refundOrder 退款订单信息
     * @param callback    处理器
     * @param <T>         返回类型
     * @return 返回支付方申请退款后的结果
     */
    <T> T refund(RefundOrder refundOrder, Callback<T> callback);

    /**
     * 查询退款
     *
     * @param refundOrder 退款订单单号信息
     * @return 返回支付方查询退款后的结果
     */
    Map<String, Object> refundquery(RefundOrder refundOrder);

    /**
     * 查询退款
     *
     * @param refundOrder 退款订单信息
     * @param callback    处理器
     * @param <T>         返回类型
     * @return 返回支付方查询退款后的结果
     */
    <T> T refundquery(RefundOrder refundOrder, Callback<T> callback);


}
