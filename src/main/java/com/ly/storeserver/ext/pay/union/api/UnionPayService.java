package com.ly.storeserver.ext.pay.union.api;

import com.ly.storeserver.ext.pay.common.bean.*;
import com.ly.storeserver.ext.pay.common.exception.PayErrorException;
import com.ly.storeserver.ext.pay.common.exception.PayException;
import com.ly.storeserver.ext.pay.common.handle.BasePayHandler;
import com.ly.storeserver.ext.pay.common.handle.Callback;
import com.ly.storeserver.ext.pay.common.http.HttpConfigStorage;
import com.ly.storeserver.ext.pay.common.util.sign.CertDescriptor;
import com.ly.storeserver.ext.pay.common.util.sign.SignUtils;
import com.ly.storeserver.ext.pay.common.util.sign.encrypt.RSA;
import com.ly.storeserver.ext.pay.common.util.sign.encrypt.RSA2;
import com.ly.storeserver.ext.pay.union.bean.SDKConstants;
import com.ly.storeserver.ext.pay.union.bean.UnionTransactionType;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * 银联支付业务
 *
 * @author : wangyu
 * @since :  2020/12/29/029 14:05
 */
public class UnionPayService extends BasePayHandler<UnionPayConfigStorage> {

    /**
     * 测试域名
     */
    private static final String TEST_BASE_DOMAIN = "test.95516.com";

    /**
     * 正式域名
     */
    private static final String RELEASE_BASE_DOMAIN = "95516.com";

    /**
     * 交易请求地址
     */
    private static final String FRONT_TRANS_URL = "https://gateway.%s/gateway/api/frontTransReq.do";
    private static final String BACK_TRANS_URL = "https://gateway.%s/gateway/api/backTransReq.do";
    private static final String SINGLE_QUERY_URL = "https://gateway.%s/gateway/api/queryTrans.do";
    private static final String BATCH_TRANS_URL = "https://gateway.%s/gateway/api/batchTrans.do";
    private static final String FILE_TRANS_URL = "https://filedownload.%s/";
    private static final String APP_TRANS_URL = "https://gateway.%s/gateway/api/appTransReq.do";
    private static final String CARD_TRANS_URL = "https://gateway.%s/gateway/api/cardTransReq.do";

    /**
     * 证书解析器
     */
    private CertDescriptor certDescriptor;

    public UnionPayService(UnionPayConfigStorage payConfigStorage) {
        this(payConfigStorage, null);
    }

    public UnionPayService(UnionPayConfigStorage payConfigStorage, HttpConfigStorage configStorage) {
        super(payConfigStorage, configStorage);
    }

    @Override
    public BasePayHandler setPayConfigStorage(UnionPayConfigStorage payConfigStorage) {
        super.setPayConfigStorage(payConfigStorage);
        certDescriptor = new CertDescriptor();
        try {
            certDescriptor.initPrivateSignCert(payConfigStorage.getKeyPrivateCertInputStream(), payConfigStorage.getKeyPrivateCertPwd(), "PKCS12");
            certDescriptor.initRootCert(payConfigStorage.getAcpMiddleCertInputStream());
            certDescriptor.initRootCert(payConfigStorage.getAcpRootCertInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 获取支付请求地址
     *
     * @param transactionType
     * @return
     */
    @Override
    public String getReqUrl(TransactionType transactionType) {
        return payConfigStorage.isTest() ? TEST_BASE_DOMAIN : RELEASE_BASE_DOMAIN;
    }

    /**
     * 根据是否为沙箱环境进行获取请求地址
     *
     * @return 请求地址
     */
    public String getReqUrl() {
        return getReqUrl(null);
    }

    public String getFrontTransUrl() {
        return String.format(FRONT_TRANS_URL, getReqUrl());
    }

    public String getBackTransUrl() {
        return String.format(BACK_TRANS_URL, getReqUrl());
    }

    public String getSingleQueryUrl() {
        return String.format(SINGLE_QUERY_URL, getReqUrl());
    }


    public String getFileTransUrl() {
        return String.format(FILE_TRANS_URL, getReqUrl());
    }


    /**
     * 回调参数校验   远端支付接口回调过来的参数
     *
     * @param params 签名校验 true通过
     */
    @Override
    public boolean verify(Map<String, Object> params) {
        return false;
    }

    /**
     * 返回创建的订单信息
     *
     * @param order 支付订单
     * @return 支付订单信息
     */
    @Override
    public <O extends PayOrder> Map<String, Object> orderInfo(O order) {
        Map<String, Object> commonParam = this.getCommonParam();
        UnionTransactionType type = (UnionTransactionType) order.getTransactionType();
        //设置交易类型相关的参数
        type.convertMap(commonParam);
        commonParam.put(SDKConstants.param_orderId, order.getOutTradeNo());
        if (StringUtils.isNotEmpty(order.getAddition())) {
            commonParam.put(SDKConstants.param_reqReserved, order.getAddition());
        }
        switch (type) {
            case WAP:
            case WEB:
            case B2B:
                commonParam.put(SDKConstants.param_txnAmt, conversionCentAmount(order.getPrice()));
                commonParam.put("orderDesc", order.getSubject());
                commonParam.put(SDKConstants.param_payTimeout, getPayTimeout(order.getExpirationTime()));

                commonParam.put(SDKConstants.param_frontUrl, payConfigStorage.getReturnUrl());
                break;
            case CONSUME:
                commonParam.put(SDKConstants.param_txnAmt, conversionCentAmount(order.getPrice()));
                commonParam.put(SDKConstants.param_qrNo, order.getAuthCode());
                break;
            case APPLY_QR_CODE:
                if (null != order.getPrice()) {
                    commonParam.put(SDKConstants.param_txnAmt, conversionCentAmount(order.getPrice()));
                }
                commonParam.put(SDKConstants.param_payTimeout, getPayTimeout(order.getExpirationTime()));
                break;
            default:
                commonParam.put(SDKConstants.param_txnAmt, conversionCentAmount(order.getPrice()));
                commonParam.put(SDKConstants.param_payTimeout, getPayTimeout(order.getExpirationTime()));
                commonParam.put("orderDesc", order.getSubject());
        }
        Map<String, Object> sign = setSign(commonParam);
        return sign;
    }

    /**
     * 生成并设置签名
     * @param parameters 请求参数
     * @return 签名值
     */
    private Map<String, Object> setSign(Map<String, Object> parameters) {
        SignUtils signUtils = SignUtils.valueOf(payConfigStorage.getSignType());
        String signStr;
        switch (signUtils) {
            case RSA:
                parameters.put(SDKConstants.param_signMethod, SDKConstants.SIGNMETHOD_RSA);
                parameters.put(SDKConstants.param_certId, certDescriptor.getSignCertId());
                signStr = SignUtils.SHA1.createSign(SignUtils.parameterText(parameters, "&", "signature"), "", payConfigStorage.getInputCharset());
                parameters.put(SDKConstants.param_signature, RSA.sign(signStr, certDescriptor.getSignCertPrivateKey(payConfigStorage.getKeyPrivateCertPwd()), payConfigStorage.getInputCharset()));
                break;
            case RSA2:
                parameters.put(SDKConstants.param_signMethod, SDKConstants.SIGNMETHOD_RSA);
                parameters.put(SDKConstants.param_certId, certDescriptor.getSignCertId());
                signStr = SignUtils.SHA256.createSign(SignUtils.parameterText(parameters, "&", "signature"), "", payConfigStorage.getInputCharset());
                parameters.put(SDKConstants.param_signature, RSA2.sign(signStr, certDescriptor.getSignCertPrivateKey(payConfigStorage.getKeyPrivateCertPwd()), payConfigStorage.getInputCharset()));
                break;
            case SHA1:
            case SHA256:
                String key = payConfigStorage.getKeyPrivate();
                signStr = SignUtils.parameterText(parameters, "&", "signature");
                key = signUtils.createSign(key, "", payConfigStorage.getInputCharset()) + "&";
                parameters.put(SDKConstants.param_signature, signUtils.createSign(signStr, key, payConfigStorage.getInputCharset()));
                break;
            default:
                throw new PayErrorException(new PayException("sign fail", "未找到的签名类型"));
        }
        return parameters;
    }

    /**
     * 元转分
     *
     * @param amount 元的金额
     * @return 分的金额
     */
    private int conversionCentAmount(BigDecimal amount) {
        return amount.multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
    }

    /**
     * 订单超时时间。
     * 超过此时间后，除网银交易外，其他交易银联系统会拒绝受理，提示超时。 跳转银行网银交易如果超时后交易成功，会自动退款，大约5个工作日金额返还到持卡人账户。
     * 此时间建议取支付时的北京时间加15分钟。
     * 超过超时时间调查询接口应答origRespCode不是A6或者00的就可以判断为失败。
     *
     * @param expirationTime 超时时间
     * @return 具体的时间字符串
     */
    private String getPayTimeout(Date expirationTime) {
        //
        return new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis() + 15 * 60 * 1000);
    }

    /**
     * 银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改
     *
     * @return 返回参数集合
     */
    private Map<String, Object> getCommonParam() {
        Map<String, Object> params = new TreeMap<>();
        UnionPayConfigStorage configStorage = payConfigStorage;
        //银联接口版本
        params.put(SDKConstants.param_version, configStorage.getVersion());
        //编码方式
        params.put(SDKConstants.param_encoding, payConfigStorage.getInputCharset().toUpperCase());
        //商户代码
        params.put(SDKConstants.param_merId, payConfigStorage.getPid());

        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        //订单发送时间
        params.put(SDKConstants.param_txnTime, df.format(System.currentTimeMillis()));
        //后台通知地址
        params.put(SDKConstants.param_backUrl, payConfigStorage.getNotifyUrl());
        //交易币种
        params.put(SDKConstants.param_currencyCode, "156");
        //接入类型，商户接入填0 ，不需修改（0：直连商户， 1： 收单机构 2：平台商户）
        params.put(SDKConstants.param_accessType, configStorage.getAccessType());
        return params;
    }

    /**
     * 获取输出消息，用户返回给支付端, 针对于web端
     *
     * @param orderInfo 发起支付的订单信息
     * @param method    请求方式  "post" "get",
     * @return 获取输出消息，用户返回给支付端, 针对于web端
     * @see MethodType 请求类型
     */
    @Override
    public String buildRequest(Map<String, Object> orderInfo, MethodType method) {
        StringBuffer sf = new StringBuffer();
        sf.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=" + payConfigStorage.getInputCharset() + "\"/></head><body>");
        sf.append("<form id = \"pay_form\" action=\"" + getFrontTransUrl() + "\" method=\"post\">");
        if (null != orderInfo && 0 != orderInfo.size()) {
            for (Map.Entry<String, Object> entry : orderInfo.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                sf.append("<input type=\"hidden\" name=\"" + key + "\" id=\"" + key + "\" value=\"" + value + "\"/>");
            }
        }
        sf.append("</form>");
        sf.append("</body>");
        sf.append("<script type=\"text/javascript\">");
        sf.append("document.all.pay_form.submit();");
        sf.append("</script>");
        sf.append("</html>");
        return sf.toString();
    }

    /**
     * 获取输出二维码信息,
     *
     * @param order 发起支付的订单信息
     * @return 返回二维码信息,，支付时需要的
     */
    @Override
    public <O extends PayOrder> String getQrPay(O order) {
        return null;
    }

    /**
     * 交易查询接口
     *
     * @param tradeNo    支付平台订单号
     * @param outTradeNo 商户单号
     * @return
     */
    @Override
    public Map<String, Object> query(String tradeNo, String outTradeNo) {
        return null;
    }

    /**
     * 交易查询接口，带处理器
     *
     * @param tradeNo    支付平台订单号
     * @param outTradeNo 商户单号
     * @param callback   处理器
     * @return 返回查询回来的结果集
     */
    @Override
    public <T> T query(String tradeNo, String outTradeNo, Callback<T> callback) {
        return null;
    }

    /**
     * 交易关闭接口
     *
     * @param tradeNo    支付平台订单号
     * @param outTradeNo 商户单号
     * @return 返回支付方交易关闭后的结果
     */
    @Override
    public Map<String, Object> close(String tradeNo, String outTradeNo) {
        return null;
    }

    /**
     * 交易关闭接口
     *
     * @param tradeNo    支付平台订单号
     * @param outTradeNo 商户单号
     * @param callback   处理器
     * @return 返回支付方交易关闭后的结果
     */
    @Override
    public <T> T close(String tradeNo, String outTradeNo, Callback<T> callback) {
        return null;
    }

    /**
     * 交易交易撤销
     *
     * @param tradeNo    支付平台订单号
     * @param outTradeNo 商户单号
     * @return 返回支付方交易撤销后的结果
     */
    @Override
    public Map<String, Object> cancel(String tradeNo, String outTradeNo) {
        return null;
    }

    /**
     * 交易交易撤销
     *
     * @param tradeNo    支付平台订单号
     * @param outTradeNo 商户单号
     * @param callback   处理器
     * @return 返回支付方交易撤销后的结果
     */
    @Override
    public <T> T cancel(String tradeNo, String outTradeNo, Callback<T> callback) {
        return null;
    }

    /**
     * 申请退款接口
     *
     * @param refundOrder 退款订单信息
     * @return 返回支付方申请退款后的结果
     */
    @Override
    public RefundResult refund(RefundOrder refundOrder) {
        return null;
    }

    /**
     * 申请退款接口
     *
     * @param refundOrder 退款订单信息
     * @param callback    处理器
     * @return 返回支付方申请退款后的结果
     */
    @Override
    public <T> T refund(RefundOrder refundOrder, Callback<T> callback) {
        return null;
    }

    /**
     * 查询退款
     *
     * @param refundOrder 退款订单单号信息
     * @return 返回支付方查询退款后的结果
     */
    @Override
    public Map<String, Object> refundquery(RefundOrder refundOrder) {
        return null;
    }

    /**
     * 查询退款
     *
     * @param refundOrder 退款订单信息
     * @param callback    处理器
     * @return 返回支付方查询退款后的结果
     */
    @Override
    public <T> T refundquery(RefundOrder refundOrder, Callback<T> callback) {
        return null;
    }
}
