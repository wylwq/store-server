package com.ly.storeserver.ext.pay.common.util.sign;

import com.ly.storeserver.ext.pay.common.bean.SignType;

import java.util.*;

/**
 * 签名工具类
 *
 * @author : wangyu
 * @since :  2020/12/28/028 10:22
 */
public enum SignUtils implements SignType {

    MD5{
        @Override
        public String createSign(String content, String key, String characterEncoding) {
            return com.ly.storeserver.ext.pay.common.util.sign.encrypt.MD5.sign(content, key, characterEncoding);
        }

        @Override
        public boolean verify(String text, String sign, String key, String characterEncoding) {
            return MD5.verify(text, sign, key, characterEncoding);
        }
    },

    RSA{
        @Override
        public String createSign(String content, String key, String characterEncoding) {
            return com.ly.storeserver.ext.pay.common.util.sign.encrypt.RSA.sign(content, key, characterEncoding);
        }

        @Override
        public boolean verify(String text, String sign, String key, String characterEncoding) {
            return com.ly.storeserver.ext.pay.common.util.sign.encrypt.RSA.verify(text, sign, key, characterEncoding);
        }
    },

    RSA2{
        @Override
        public String createSign(String content, String key, String characterEncoding) {
            return com.ly.storeserver.ext.pay.common.util.sign.encrypt.RSA2.sign(content, key, characterEncoding);
        }

        @Override
        public boolean verify(String text, String sign, String key, String characterEncoding) {
            return com.ly.storeserver.ext.pay.common.util.sign.encrypt.RSA2.verify(text, sign, key, characterEncoding);
        }
    },

    SHA1 {
        @Override
        public String createSign(String content, String key, String characterEncoding) {
            return com.ly.storeserver.ext.pay.common.util.sign.encrypt.SHA1.sign(content, key, characterEncoding);
        }

        @Override
        public boolean verify(String text, String sign, String publicKey, String characterEncoding) {
            return com.ly.storeserver.ext.pay.common.util.sign.encrypt.SHA1.verify(text, sign, publicKey, characterEncoding);
        }
    },
    SHA256 {
        @Override
        public String createSign(String content, String key, String characterEncoding) {
            return com.ly.storeserver.ext.pay.common.util.sign.encrypt.SHA256.sign(content, key, characterEncoding);
        }

        @Override
        public boolean verify(String text, String sign, String publicKey, String characterEncoding) {
            return com.ly.storeserver.ext.pay.common.util.sign.encrypt.SHA256.verify(text, sign, publicKey, characterEncoding);
        }
    },



    ;

    /**
     * 获取签名类型名称
     *
     * @return
     */
    @Override
    public String getName() {
        return this.name();
    }

    /**
     *
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“@param separator”字符拼接成字符串
     * @param parameters 参数
     * @param separator 分隔符
     * @param ignoreKey 需要忽略添加的key
     * @return 去掉空值与签名参数后的新签名，拼接后字符串
     */
    public static String parameterText(Map parameters, String separator, String... ignoreKey) {
        return parameterText(parameters, separator, true, ignoreKey);
    }

    /**
     *
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“@param separator”字符拼接成字符串
     * @param parameters 参数
     * @param separator 分隔符
     * @param ignoreNullValue 需要忽略NULL值
     * @param ignoreKey 需要忽略添加的key
     * @return 去掉空值与签名参数后的新签名，拼接后字符串
     */
    public static String parameterText(Map parameters, String separator, boolean ignoreNullValue, String... ignoreKey ) {
        if(parameters == null){
            return "";
        }
        StringBuffer sb = new StringBuffer();
        if (null != ignoreKey){
            Arrays.sort(ignoreKey);
        }

        if (parameters instanceof SortedMap) {
            for (Map.Entry<String, Object> entry : (Set<Map.Entry<String, Object>>)parameters.entrySet()) {
                Object v = entry.getValue();
                if (null == v || "".equals(v.toString().trim()) || (null != ignoreKey && Arrays.binarySearch(ignoreKey, entry.getKey() ) >= 0)) {
                    continue;
                }
                sb.append(entry.getKey() ).append("=").append( v.toString().trim()).append(separator);
            }
            if (sb.length() > 0 && !"".equals(separator)) {
                sb.deleteCharAt(sb.length() - 1);
            }
            return sb.toString();

        }

        List<String> keys = new ArrayList<String>(parameters.keySet());
        //排序
        Collections.sort(keys);
        for (String k : keys) {
            String valueStr = "";
            Object o = parameters.get(k);
            if (ignoreNullValue && null == o) {
                continue;
            }
            if (o instanceof String[]) {
                String[] values = (String[]) o;

                for (int i = 0; i < values.length; i++) {
                    String value = values[i].trim();
                    if ("".equals(value)){ continue;}
                    valueStr += (i == values.length - 1) ?  value :  value + ",";
                }
            } else {
                valueStr = o.toString();
            }
            if (null == valueStr || "".equals(valueStr.toString().trim()) || (null != ignoreKey && Arrays.binarySearch(ignoreKey, k ) >= 0)) {
                continue;
            }
            sb.append(k ).append("=").append( valueStr).append(separator);
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

}
