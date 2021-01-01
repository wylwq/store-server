package com.ly.storeserver.ext.pay.common.http;

import com.ly.storeserver.ext.pay.common.bean.CertStoreType;
import lombok.Data;

import java.io.IOException;
import java.io.InputStream;

/**
 * http请求参数配置类
 *
 * @author : wangyu
 * @since :  2020/12/27/027 22:20
 */
@Data
public class HttpConfigStorage {

    /**
     * http代理地址
     */
    private String httpProxyHost;

    /**
     * 代理端口
     */
    private Integer httpProxyPort;

    /**
     * 请求授权用户名
     */
    private String authUsername;

    /**
     * 请求授权密码
     */
    private String authPassword;

    /**
     * 证书存储类型
     * @see #keystore 是否为https请求说需要的证书(PKCS12)的地址
     */
    private CertStoreType certStoreType = CertStoreType.PATH;

    /**
     * https请求说需要的证书(PKCS12)
     * 证书内容
     */
    private Object keystore;

    /**
     * 证书对应的密码
     */
    private String storePassword;

    /**
     * 最大连接数
     */
    private Integer maxTotal = 0;

    /**
     * 默认的每个路由最大连接数
     */
    private Integer defaultMaxPreRoute = 0;

    /**
     * 默认使用的响应编码
     */
    private String charset;

    private Integer socketTimeout = -1;

    private Integer connectTimeout = -1;

    /**
     * 获取证书信息
     *
     * @return 证书信息 根据{@link #getCertStoreType()}进行区别地址与信息串
     * @throws IOException 找不到文件异常
     */
    public InputStream getKeystoreInputStream() throws IOException {
        if (null == keystore) {
            return null;
        }
        return certStoreType.getInputStream(keystore);
    }
}
