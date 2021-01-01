package com.ly.storeserver.ext.pay.common.bean;

import com.ly.storeserver.ext.pay.common.handle.CertStore;
import com.ly.storeserver.ext.pay.common.http.HttpRequestTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 证书存储类型
 *
 * @author : wangyu
 * @since :  2020/12/27/027 22:30
 */
public enum CertStoreType implements CertStore {

    /**
     * 无存储类型，表示无需要转换为输入流
     */
    NONE{
        /**
         * 证书信息转换为对应的输入流
         *
         * @param cert 证书信息
         * @return 输入流
         * @throws IOException 找不到文件异常
         */
        @Override
        public InputStream getInputStream(Object cert) throws IOException {
            return null;
        }
    },

    /**
     * URL获取的方式
     */
    URL {
        /**
         * 证书信息转化为对应的输入流
         *
         * @param url 获取证书信息的URL
         * @return 输入流
         * @throws IOException 找不到文件异常
         */
        @Override
        public InputStream getInputStream(Object url) throws IOException {
            return new HttpRequestTemplate().getForObject((String) url, InputStream.class);
        }
    },

    /**
     * class路径
     */
    CLASS_PATH{
        /**
         * 证书信息转换为对应的输入流
         * @param cert
         * @return
         * @throws IOException
         */
        @Override
        public InputStream getInputStream(Object cert) throws IOException {
            InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream((String) cert);
            return resourceAsStream;
        }
    },

    /**
     * 文件路径，建议绝对路径
     */
    PATH{
        /**
         * 证书信息转换为对应的输入流
         *
         * @param cert 证书信息
         * @return 输入流
         * @throws IOException 找不到文件异常
         */
        @Override
        public InputStream getInputStream(Object cert) throws IOException {
            return new FileInputStream(new File((String) cert));
        }
    },

    ;


}
