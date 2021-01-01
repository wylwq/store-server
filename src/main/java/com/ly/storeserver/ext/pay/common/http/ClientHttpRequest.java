package com.ly.storeserver.ext.pay.common.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.ly.storeserver.ext.pay.common.bean.MethodType;
import com.ly.storeserver.ext.pay.common.exception.PayErrorException;
import com.ly.storeserver.ext.pay.common.exception.PayException;
import com.ly.storeserver.utils.XML;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * http客户端请求
 *
 * @author : wangyu
 * @since :  2020/12/29/029 10:08
 */
public class ClientHttpRequest<T> extends HttpEntityEnclosingRequestBase implements ResponseHandler<T> {

    public static final ContentType APPLICATION_FORM_URLENCODED_UTF_8 = ContentType.create("application/x-www-form-urlencoded", Consts.UTF_8);
    public static final ContentType APPLICATION_XML_UTF_8 = ContentType.create("application/xml", Consts.UTF_8);

    /**
     * http请求方式 get post
     */
    private MethodType method;

    /**
     * 默认使用的响应编码
     */
    private Charset defaultCharset;

    /**
     * 响应类型
     */
    private Class<T> responseType;

    public ClientHttpRequest<T> setResponseType(Class<T> responseType) {
        this.responseType = responseType;
        return this;
    }

    /**
     * 根据请求地址，请求方法，请求参数构造请求对象
     *
     * @param uri 请求地址
     * @param method 请求方法
     * @param request 请求参数
     * @param charset 字符编码
     */
    public ClientHttpRequest(URI uri, MethodType method, Object request, String charset) {
        this(uri, method);
        setParameters(request);
        if (StringUtils.isNotEmpty(charset)) {
            setDefaultCharset(Charset.forName(charset));
        }
    }

    public ClientHttpRequest(String uri, MethodType method) {
        this.setURI(URI.create(uri));
        this.method = method;
    }

    public ClientHttpRequest(URI uri, MethodType method) {
        this.setURI(uri);
        this.method = method;
    }

    public void setDefaultCharset(Charset defaultCharset) {
        this.defaultCharset = defaultCharset;
    }

    public Charset getDefaultCharset() {
        if (null == defaultCharset) {
            defaultCharset = Consts.UTF_8;
        }
        return defaultCharset;
    }

    /**
     * 设置代理
     *
     * @param httpProxy http代理配置信息
     * @return 当前HTTP请求的客户端
     */
    public ClientHttpRequest setProxy(HttpHost httpProxy) {
        if (httpProxy != null) {
            RequestConfig config = RequestConfig.custom().setProxy(httpProxy).build();
            setConfig(config);
        }
        return this;
    }

    @Override
    public T handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
        final StatusLine statusLine = response.getStatusLine();
        final HttpEntity entity = response.getEntity();

        String[] value = null;
        if (null == entity.getContentType()) {
            value = new String[]{"application/x-www-form-urlencoded"};
        } else {
            value = entity.getContentType().getValue().split(";");
        }
        //这里进行特殊处理，如果状态码非正常状态，但内容类型匹配至对应的结果也进行对应的响应类型转换
        if (statusLine.getStatusCode() >= 300 && statusLine.getStatusCode() != 304) {
            if (isJson(value[0], "") || isXml(value[0], "")) {
                return toBean(entity, value);
            }

            EntityUtils.consume(entity);
            throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
        }
        if (null == responseType) {
            responseType = (Class<T>) String.class;
        }
        return toBean(entity, value);

    }

    /**
     * 对请求进行转化至对应的可转化类型
     *
     * @param entity      响应实体
     * @param contentType 内容类型编码数组，第一个值为内容类型，第二个值为编码类型
     * @return 对应的响应对象
     * @throws IOException 响应类型文本转换时抛出异常
     */
    private T toBean(HttpEntity entity, String[] contentType) throws IOException {
        //判断内容类型是否为文本类型
        if (isText(contentType[0])) {
/*            String charset = "UTF-8";
            if (null != contentType && 2 == charset.length()) {
                charset = contentType[1].substring(contentType[1].indexOf("=") + 1);
            }*/

            //获取响应的文本内容
            String result = EntityUtils.toString(entity, getDefaultCharset());
            if (responseType.isAssignableFrom(String.class)) {
                return (T) result;
            }

            String first = result.substring(0, 1);
            //json类型
            if (isJson(contentType[0], first)) {
                try {
                    if (responseType.isAssignableFrom(JSONObject.class)) {
                        return (T)JSON.parseObject(result);
                    }
                    return JSON.parseObject(result, responseType);
                } catch (JSONException e) {
                    throw new PayErrorException(new PayException("failure", String.format("类型转化异常,contentType: %s\n%s", entity.getContentType().getValue(), e.getMessage()), result));
                }
            }
            //xml类型
            if (isXml(contentType[0], first)) {
                try {
                    if (responseType.isAssignableFrom(JSONObject.class)) {
                        return (T) XML.toJSONObject(result, getDefaultCharset());
                    }
                    return XML.toJSONObject(result, getDefaultCharset()).toJavaObject(responseType);
                } catch (Exception e) {
                    ;
                }
            }
            throw new PayErrorException(new PayException("failure", "类型转化异常,contentType:" + entity.getContentType().getValue(), result));
        }

        //是否为 输入流
        if (InputStream.class.isAssignableFrom(responseType)) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            entity.writeTo(os);
            return (T) new ByteArrayInputStream(os.toByteArray());
        }
        //是否为 字节数数组
        if (byte[].class.isAssignableFrom(responseType)) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            entity.writeTo(os);
            return (T) os.toByteArray();
        }
        //输出流
        if (OutputStream.class.isAssignableFrom(responseType)) {
            try {
                OutputStream t;
                if (responseType == OutputStream.class){
                    t= new ByteArrayOutputStream();
                }else {
                    t = (OutputStream) responseType.newInstance();
                }
                entity.writeTo( t);
                return (T) t;
            } catch (InstantiationException e) {
                throw new PayErrorException(new PayException("InstantiationException", e.getMessage()));
            } catch (IllegalAccessException e) {
                throw new PayErrorException(new PayException("IllegalAccessException", e.getMessage()));
            }
        }
        throw new PayErrorException(new PayException("failure", "类型转化异常,contentType:" + entity.getContentType().getValue()));
    }

    /**
     * 检测响应类型是否为json
     *
     * @param contentType 内容类型
     * @param textFirst   文本第一个字符
     * @return 布尔型， true为json内容类型
     */
    private boolean isJson(String contentType, String textFirst) {
        return (ContentType.APPLICATION_JSON.getMimeType().equals(contentType) || "{[".indexOf(textFirst) >= 0);
    }

    /**
     * 检测响应类型是否为文本类型
     *
     * @param contentType 内容类型
     * @return 布尔型， true为文本内容类型
     */
    private boolean isText(String contentType) {
        return contentType.contains("xml") || contentType.contains("json") || contentType.contains("text") || contentType.contains("form-data") || contentType.contains("x-www-form-urlencoded");
    }

    /**
     * 检测响应类型是否为xml
     *
     * @param contentType 内容类型
     * @param textFirst   文本第一个字符
     * @return 布尔型， true为xml内容类型
     */
    private boolean isXml(String contentType, String textFirst) {
        return (ContentType.APPLICATION_XML.getMimeType().equals(contentType) || "<".indexOf(textFirst) >= 0);
    }

    @Override
    public String getMethod() {
        return method.name();
    }

    /**
     * 设置请求参数
     *
     * @param request 请求参数
     * @return 当前HTTP请求的客户端
     */
    public ClientHttpRequest setParameters(Object request) {
        if (null == request) {
            return this;
        }
        if (request instanceof HttpHeader) {
            HttpHeader entity = (HttpHeader) request;
            if (null != entity.getHeaders()) {
                for (Header header : entity.getHeaders()) {
                    addHeader(header);
                }
            }
        } else if (request instanceof HttpStringEntity) {
            HttpStringEntity entity = (HttpStringEntity) request;
            if (!entity.isEmpty()) {
                setEntity(entity);
            }
            if (null != entity.getHeaders()) {
                for (Header header : entity.getHeaders()) {
                    addHeader(header);
                }
            }
        } else if (request instanceof HttpEntity) {
            setEntity((HttpEntity) request);
        } else if (request instanceof Map) {
            String parameters = MapToParameterUtil.getMapToParameters((Map) request);
            StringEntity entity = new StringEntity(parameters, APPLICATION_FORM_URLENCODED_UTF_8);
            setEntity(entity);
        } else if (request instanceof String) {
            StringEntity entity = new StringEntity((String) request, APPLICATION_FORM_URLENCODED_UTF_8);
            setEntity(entity);
        } else {
            String body = JSON.toJSONString(request);
            StringEntity entity = new StringEntity(body, ContentType.APPLICATION_JSON);
            setEntity(entity);
        }
        return this;

    }
}
