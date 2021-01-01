package com.ly.storeserver.ext.pay.common.util.sign;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

/**
 * 证书工具类 主要用于对证书的加载和使用
 *
 * @author : wangyu
 * @since :  2020/12/29/029 14:09
 */
public class CertDescriptor {

    /**
     * 证书容器，存储对商户请求报文签名私钥证书
     */
    private KeyStore keyStore = null;

    /**
     * 验证公钥/中级证书
     */
    private X509Certificate publicKeyCert = null;

    /**
     * 验签根证书
     */
    private X509Certificate rootKeyCert = null;

    public CertDescriptor() {

    }

    /**
     * 通过证书路径初始化为公钥证书
     *
     * @param certIn 证书流
     * @return X509证书
     */
    private static X509Certificate init(InputStream certIn) {
        X509Certificate x509Certificate = null;
        try {
            CertificateFactory instance = CertificateFactory.getInstance("X.509");
            x509Certificate =(X509Certificate) instance.generateCertificate(certIn);
        } catch (CertificateException e) {
            e.printStackTrace();
        } finally {
            if (certIn != null) {
                try {
                    certIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return x509Certificate;
    }

    /**
     * 通过证书路径初始化为公钥证书
     *
     * @param path 证书路径
     * @return X509证书
     */
    private static X509Certificate init(String path) {
        X509Certificate x509Certificate = null;
        InputStream certIn;
        try {
            certIn = new FileInputStream(path);
            x509Certificate = init(certIn);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return x509Certificate;
    }

    /**
     * 通过keyStore 获取私钥签名证书PrivateKey对象
     *
     * @param pwd 证书对应的密码
     * @return privateKey 私钥
     */
    public PrivateKey getSignCertPrivateKey(String pwd) {
        try {
            Enumeration<String> aliases = keyStore.aliases();
            String keyAlias = null;
            while (aliases.hasMoreElements()) {
                keyAlias = aliases.nextElement();
            }
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(keyAlias, pwd.toCharArray());
            return privateKey;
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 配置的签名私钥证书certId
     *
     * @return 证书的物理编号
     */
    public String getSignCertId() {
        try {
            Enumeration<String> aliases = keyStore.aliases();
            String keyAlias = null;
            if (aliases.hasMoreElements()) {
                keyAlias = aliases.nextElement();
            }
            X509Certificate certificate =(X509Certificate) keyStore.getCertificate(keyAlias);
            return certificate.getSerialNumber().toString();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将签名私钥证书文件读取为证书存储对象
     *
     * @param signCert 证书文件
     * @param signCertPwd 证书密码
     * @param signCertType 证书类型
     */
    public void initPrivateSignCert(InputStream signCert, String signCertPwd, String signCertType) {
        if (keyStore != null) {
            keyStore = null;
        }
        try {
            keyStore = getKeyInfo(signCert, signCertPwd, signCertType);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载中级证书
     *
     * @param cert 证书文件
     */
    public void initPublicCert(InputStream cert) {
        if (null != cert) {
            publicKeyCert = init(cert);
        }
    }

    /**
     * 加载根证书
     *
     * @param cert 证书文件
     */
    public void initRootCert(InputStream cert) {
        if (null != cert) {
            rootKeyCert = init(cert);
        }
    }


    /**
     * 将签名私钥证书文件读取为证书存储对象
     *
     * @param signCert 证书文件
     * @param signCertPwd 证书密码
     * @param signCertType 证书类型
     * @return 证书对象
     */
    private KeyStore getKeyInfo(InputStream signCert, String signCertPwd, String signCertType) throws KeyStoreException {
        KeyStore keyStore = KeyStore.getInstance(signCertType);
        try {
            keyStore.load(signCert, signCertPwd.toCharArray());
            return keyStore;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } finally {
            if (null != signCert) {
                try {
                    signCert.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
