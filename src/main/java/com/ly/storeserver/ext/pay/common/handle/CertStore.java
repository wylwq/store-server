package com.ly.storeserver.ext.pay.common.handle;

import java.io.IOException;
import java.io.InputStream;

/**
 * 证书存储方式
 *
 * @author : wangyu
 * @since :  2020/12/27/027 22:31
 */
public interface CertStore {

    /**
     * 证书信息转换为对应的输入流
     * @param cert
     * @return
     * @throws IOException
     */
    InputStream getInputStream(Object cert) throws IOException;
}
