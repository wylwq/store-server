package com.ly.storeserver.ext.pay.common.http;

import com.ly.storeserver.ext.pay.common.bean.MethodType;
import com.ly.storeserver.ext.pay.common.exception.PayErrorException;
import com.ly.storeserver.ext.pay.common.exception.PayException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * http请求工具类
 *
 * @author : wangyu
 * @since :  2020/12/27/027 22:19
 */
public class HttpRequestTemplate {

    protected CloseableHttpClient httpClient;

    protected PoolingHttpClientConnectionManager connectionManager;

    protected HttpHost httpProxy;

    protected HttpConfigStorage configStorage;

    private SSLConnectionSocketFactory sslsf;

    /**
     * 获取代理带代理的HttpHost
     * @return 获取代理带代理地址的 HttpHost
     */
    public HttpHost getHttpProxy() {
        return httpProxy;
    }

    public CloseableHttpClient getHttpClient() {
        if (null != httpClient) {
            return httpClient;
        }
        if (null == configStorage) {
            return httpClient = HttpClients.createDefault();
        }

        CloseableHttpClient httpClient = HttpClients
                .custom()
                .setDefaultCredentialsProvider(createCredentialsProvider(configStorage))
                .setConnectionManager(connectionManager(configStorage))
                .setSSLSocketFactory(createSSL(configStorage))
                .setDefaultRequestConfig(createRequestConfig(configStorage))
                .build();
        if (null == connectionManager) {
            return this.httpClient = httpClient;
        }

        return httpClient;
    }

    public HttpRequestTemplate(HttpConfigStorage configStorage) {
        setHttpConfigStorage(configStorage);
    }

    public HttpRequestTemplate() {
        setHttpConfigStorage(null);
    }

    /**
     * 设置HTTP请求的配置
     *
     * @param configStorage 请求配置
     * @return 当前HTTP请求的客户端模板
     */
    public HttpRequestTemplate setHttpConfigStorage(HttpConfigStorage configStorage) {
        this.configStorage = configStorage;
        if (null != configStorage && StringUtils.isNotBlank(configStorage.getHttpProxyHost())) {
            httpProxy = new HttpHost(configStorage.getHttpProxyHost(), configStorage.getHttpProxyPort());
        }
        return this;
    }

    private RequestConfig createRequestConfig(HttpConfigStorage configStorage) {
        RequestConfig config = RequestConfig.custom()
                .setSocketTimeout(configStorage.getSocketTimeout())
                .setConnectTimeout(configStorage.getConnectTimeout())
                .build();
        return config;
    }

    /**
     * 创建凭证提供程序
     *
     * @param configStorage 请求配置
     * @return 凭证提供程序
     */
    private CredentialsProvider createCredentialsProvider(HttpConfigStorage configStorage) {

        if (StringUtils.isBlank(configStorage.getAuthUsername())) {
            return null;
        }
        //需要用户认证的代理服务器
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(
                AuthScope.ANY,
                new UsernamePasswordCredentials(configStorage.getAuthUsername(), configStorage.getAuthPassword())
        );

        return credentialsProvider;
    }

    /**
     * 初始化连接池
     *
     * @param configStorage 配置
     * @return 连接池对象
     */
    private PoolingHttpClientConnectionManager connectionManager(HttpConfigStorage configStorage) {
        if (null != connectionManager) {
            return connectionManager;
        }
        if (0 == configStorage.getMaxTotal() || 0 == configStorage.getDefaultMaxPreRoute()) {
            return null;
        }
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("https", createSSL(configStorage))
                .register("http", new PlainConnectionSocketFactory())
                .build();
        connectionManager = new PoolingHttpClientConnectionManager(registry);
        connectionManager.setMaxTotal(configStorage.getMaxTotal());
        connectionManager.setDefaultMaxPerRoute(configStorage.getDefaultMaxPreRoute());
        return connectionManager;
    }

    /**
     * 创建ssl配置
     *
     * @param configStorage
     * @return
     */
    private SSLConnectionSocketFactory createSSL(HttpConfigStorage configStorage) {
        if (null != sslsf) {
            return sslsf;
        }
        if (null == configStorage.getKeystore()) {
            try {
                return sslsf = new SSLConnectionSocketFactory(SSLContext.getDefault());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        //读取本机存放的PKCS12证书文件
        try(InputStream inputStream = configStorage.getKeystoreInputStream()) {
            //指定读取证书格式为PKCS12证书文件
            KeyStore pkcs12 = KeyStore.getInstance("PKCS12");
            char[] chars = configStorage.getStorePassword().toCharArray();
            pkcs12.load(inputStream, chars);
            //创建SSLContext
            SSLContext sslContext = SSLContexts.custom()
                    .loadKeyMaterial(pkcs12, chars)
                    .build();

            //指定TLS版本
            sslsf = new SSLConnectionSocketFactory(
                    sslContext,
                    new String[]{"TLSv1"},
                    null,
                    new DefaultHostnameVerifier()
            );
            return sslsf;
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public <T> T postForObject(String uri, Object request, Class<T> responseType) {
        return doExecute(URI.create(uri), request, responseType, MethodType.POST);
    }

    /**
     * 执行远程调用的请求方法
     *
     * @param uri 请求uri
     * @param request 请求参数
     * @param responseType 响应类型
     * @param methodType 请求方法
     * @param <T> 返回值类型
     * @return 返回值
     */
    private <T> T doExecute(URI uri, Object request, Class<T> responseType, MethodType methodType) {
        ClientHttpRequest<T> clientHttpRequest = new ClientHttpRequest<>(uri, methodType, request, null == configStorage ? null : configStorage.getCharset());
        if (httpProxy != null) {
            clientHttpRequest.setProxy(httpProxy);
        }
        clientHttpRequest.setResponseType(responseType);
        try(CloseableHttpResponse execute = getHttpClient().execute(clientHttpRequest)) {
            return clientHttpRequest.handleResponse(execute);
        } catch (IOException e) {
            throw new PayErrorException(new PayException("IOException", e.getLocalizedMessage()));
        } finally {
            clientHttpRequest.releaseConnection();
        }
    }

    /**
     * get 请求
     *
     * @param uri          请求地址
     * @param responseType 响应类型
     * @param uriVariables 用于匹配表达式
     * @param <T>          响应类型
     *
     * @return 类型对象
     * <p>
     * <code>
     * getForObject(&quot;http://egan.in/pay/{id}/f/{type}&quot;, String.class, &quot;1&quot;, &quot;APP&quot;)
     * </code>
     */
    public <T> T getForObject(String uri, Class<T> responseType, Object... uriVariables){

        return doExecute(URI.create(uri), null, responseType, MethodType.GET);
    }
}
