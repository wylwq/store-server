package com.ly.storeserver.ext.pay.controller;

import cn.hutool.core.util.IdUtil;
import com.ly.storeserver.common.annotation.Token;
import com.ly.storeserver.ext.pay.common.bean.CertStoreType;
import com.ly.storeserver.ext.pay.common.bean.PayOrder;
import com.ly.storeserver.ext.pay.common.http.HttpConfigStorage;
import com.ly.storeserver.ext.pay.common.util.sign.SignUtils;
import com.ly.storeserver.ext.pay.union.api.UnionPayConfigStorage;
import com.ly.storeserver.ext.pay.union.api.UnionPayService;
import com.ly.storeserver.ext.pay.union.bean.UnionTransactionType;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * 银联支付测试controller
 *
 * @author : wangyu
 * @since :  2020/12/29/029 15:31
 */
@RestController
@RequestMapping("/union/")
@Api(value = "银联测试controller", tags = "银联测试controller")
public class UnionPayController {

    private UnionPayService unionPayService;

    @PostConstruct
    public void init(){
        UnionPayConfigStorage unionPayConfigStorage = new UnionPayConfigStorage();
        unionPayConfigStorage.setMerId("700000000000001");
        //是否为证书签名
        unionPayConfigStorage.setCertSign(true);
        //中级证书路径
        unionPayConfigStorage.setAcpMiddleCert("acp_test_middle.cer");
        //根证书路径
        unionPayConfigStorage.setAcpRootCert("acp_test_root.cer");
        // 私钥证书路径
        unionPayConfigStorage.setKeyPrivateCert("acp_test_sign.pfx");
        //私钥证书对应的密码
        unionPayConfigStorage.setKeyPrivateCertPwd("000000");
        //设置证书对应的存储方式，默认支持位文件地址
        unionPayConfigStorage.setCertStoreType(CertStoreType.CLASS_PATH);
        //前台通知网址  即SDKConstants.param_frontUrl
        unionPayConfigStorage.setReturnUrl("http://8.129.39.194/xmshop/");
        //后台通知地址  即SDKConstants.param_backUrl
        unionPayConfigStorage.setNotifyUrl("http://union.free.idcfengye.com/pay/notify/v1/union/pay_notify");
        //加密方式
        unionPayConfigStorage.setSignType(SignUtils.RSA2.getName());
        //单一支付可不填
        unionPayConfigStorage.setPayType("unionPay");
        unionPayConfigStorage.setInputCharset("UTF-8");
        //是否为测试账号，沙箱环境
        unionPayConfigStorage.setTest(true);
        unionPayService = new UnionPayService(unionPayConfigStorage);
        //请求连接池配置
        HttpConfigStorage httpConfigStorage = new HttpConfigStorage();
        //最大连接数
        httpConfigStorage.setMaxTotal(20);
        //默认的每个路由的最大连接数
        httpConfigStorage.setDefaultMaxPreRoute(10);
        unionPayService.setHttpRequestTemplateConfigStorage(httpConfigStorage);
    }

    @RequestMapping(value = "toPay.html", produces = "text/html;charset=UTF-8")
    @Token
    public String toPay() {
        PayOrder payOrder = new PayOrder("订单title", "摘要", BigDecimal.valueOf(0.01), IdUtil.fastSimpleUUID(), UnionTransactionType.WEB);
        return unionPayService.toPay(payOrder);
    }
}
