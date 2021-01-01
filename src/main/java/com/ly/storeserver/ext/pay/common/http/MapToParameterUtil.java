package com.ly.storeserver.ext.pay.common.http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * map转换为parameter
 *
 * @author : wangyu
 * @since :  2020/12/29/029 10:41
 */
public class MapToParameterUtil {

    /**
     * Map转化为对应得参数字符串
     * @param pe 参数
     * @return 参数字符串
     */
    public static String getMapToParameters(Map pe){
        StringBuilder builder = new StringBuilder();
        for (Map.Entry entry : (Set<Map.Entry>)pe.entrySet()) {
            Object o = entry.getValue();

            if (null == o) {
                continue;
            }

            if (o instanceof List) {
                o = ((List) o).toArray();
            }
            try {
                if (o instanceof Object[]) {
                    Object[] os = (Object[]) o;
                    String valueStr = "";
                    for (int i = 0, len = os.length; i < len; i++) {
                        if (null == os[i]) {
                            continue;
                        }
                        String value = os[i].toString().trim();
                        valueStr += (i == len - 1) ?  value :  value + ",";
                    }
                    builder.append(entry.getKey()).append("=").append(URLEncoder.encode(valueStr, "utf-8")).append("&");

                    continue;
                }
                builder.append(entry.getKey()).append("=").append(URLEncoder.encode( entry.getValue().toString(), "utf-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        if (builder.length() > 1) {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }
}
