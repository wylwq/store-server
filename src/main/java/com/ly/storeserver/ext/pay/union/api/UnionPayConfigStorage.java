package com.ly.storeserver.ext.pay.union.api;

import com.ly.storeserver.ext.pay.common.bean.CertStoreType;
import com.ly.storeserver.ext.pay.common.handle.BasePayConfigStorage;

import java.io.IOException;
import java.io.InputStream;

/**
 * 银联支付对接参数配置类
 *
 * @author : wangyu
 * @since :  2020/12/29/029 11:34
 */
public class UnionPayConfigStorage extends BasePayConfigStorage {

    /**
     * 商户号
     */
    private String merId;

    /**
     * 商户收款账号
     */
    private String seller;

    private String version = "5.1.0";

    /**
     * 0:普通商户直接接入
     * 1:收单机构
     * 2:平台类商户接入
     */
    private String accessType = "0";

    /**
     * 应用私钥证书
     */
    private Object keyPrivateCert;

    /**
     * 应用私钥证书，rsa_private kpcs8格式 生成签名时使用
     */
    private String keyPrivateCertPwd;

    /**
     * 中级证书
     */
    private Object acpMiddleCert;

    /**
     * 根证书
     */
    private Object acpRootCert;

    /**
     * 证书存储类型
     */
    private CertStoreType certStoreType;

    /**
     * 设置私钥证书
     *
     * @param certifica 私钥证书地址或者证书内容字符串 私钥证书密码
     */
    public void setKeyPrivateCert(String certifica) {
        super.setKeyPrivate(certifica);
        this.keyPrivateCert = certifica;
    }

    public void setKeyPrivateCert(InputStream keyPrivateCert) {
        this.keyPrivateCert = keyPrivateCert;
    }

    /**
     * 设置中级证书
     * @param acpMiddleCert
     */
    public void setAcpMiddleCert(String acpMiddleCert) {
        this.acpMiddleCert = acpMiddleCert;
    }

    public void setAcpMiddleCert(InputStream acpMiddleCert) {
        this.acpMiddleCert = acpMiddleCert;
    }

    /**
     * 设置根证书
     *
     * @param acpRootCert 证书路径或者证书信息字符串
     */
    public void setAcpRootCert(String acpRootCert) {
        this.acpRootCert = acpRootCert;
    }
    /**
     * 设置根证书
     *
     * @param acpRootCert 证书文件流
     */
    public void setAcpRootCert(InputStream acpRootCert) {
        this.acpRootCert = acpRootCert;
    }

    public String getAcpMiddleCert() {
        return (String) acpMiddleCert;
    }

    public String getAcpRootCert() {
        return (String) acpRootCert;
    }
    public InputStream getAcpMiddleCertInputStream() throws IOException {
        return certStoreType.getInputStream(acpMiddleCert);
    }

    public InputStream getAcpRootCertInputStream() throws IOException {
        return certStoreType.getInputStream(acpRootCert);
    }

    /**
     * 获取私钥证书密码
     *
     * @return 私钥证书密码
     */
    public String getKeyPrivateCertPwd() {
        return keyPrivateCertPwd;
    }

    public void setKeyPrivateCertPwd(String keyPrivateCertPwd) {
        this.keyPrivateCertPwd = keyPrivateCertPwd;
    }


    /**
     * 应用id
     *
     * @return 应用id
     */
    @Override
    public String getAppid() {
        return null;
    }

    /**
     * 合作商唯一标识
     *
     * @return 合作商唯一标识
     */
    @Override
    public String getPid() {
        return merId;
    }

    /**
     * 获取收款账号
     *
     * @return 收款账号
     */
    @Override
    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getMerId() {
        return merId;
    }

    public void setMerId(String merId) {
        this.merId = merId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    /**
     * 证书存储类型
     * @return 证书存储类型
     */
    public CertStoreType getCertStoreType() {
        return certStoreType;
    }

    public void setCertStoreType(CertStoreType certStoreType) {
        this.certStoreType = certStoreType;
    }

    public InputStream getKeyPrivateCertInputStream() throws IOException {
        return certStoreType.getInputStream(keyPrivateCert);
    }


}
