package com.ly.storeserver.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ly.storeserver.common.constant.AdminConstants;
import com.ly.storeserver.common.constant.YPConstants;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author ly
 * @Date 2020/4/3 15:17
 * @Version V1.0.0
 **/
public class CodeUtil {

    /**
     * 默认生成6位验证码
     * @return
     */
    public static String code() {
        return code(6);
    }

    /**
     * 生成指定位数的验证码
     * @param num
     * @return
     */
    public static String code(int num) {
        if (num > 0 && num < 32) {
            StringBuffer codeBuffer = new StringBuffer(num);
            for (int i = 0; i < num; i ++) {
                int random = (int) (Math.random() * 10);
                codeBuffer.append(random);
            }
            String code = codeBuffer.toString();
            if ("0".startsWith(code)) {
                return code(num);
            }
            return code;
        }
        return AdminConstants.DEFAULT_CODE;
    }

    /**
     * 获取登录用户的IP地址
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.equals("0:0:0:0:0:0:0:1")) {
            ip = "127.0.0.1";
        }
        if (ip.split(",").length > 1) {
            ip = ip.split(",")[0];
        }
        return ip;
    }

    /**
     * 通过IP获取地址(需要联网，调用淘宝的IP库)
     * @param ip
     * @return
     */
    public static String getIpInfo(String ip) {
        if ("127.0.0.1".equals(ip)) {
            ip = "127.0.0.1";
        }
        String info = "";
        try {
            URL url = new URL("http://ip.taobao.com/service/getIpInfo.php?ip=" + ip);
            HttpURLConnection htpcon = (HttpURLConnection) url.openConnection();
            htpcon.setRequestMethod("GET");
            htpcon.setDoOutput(true);
            htpcon.setDoInput(true);
            htpcon.setUseCaches(false);

            InputStream in = htpcon.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            StringBuffer temp = new StringBuffer();
            String line = bufferedReader.readLine();
            while (line != null) {
                temp.append(line).append("\r\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            JSONObject obj = (JSONObject) JSON.parse(temp.toString());
            if (obj.getIntValue("code") == 0) {
                JSONObject data = obj.getJSONObject("data");
                info += data.getString("country") + " ";
                info += data.getString("region") + " ";
                info += data.getString("city") + " ";
                info += data.getString("isp");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    /**
     * 智能匹配模板接口发短信
     *
     * @param apikey apikey
     * @param mobile 　接受的手机号
     * @return json格式字符串
     * @throws IOException
     */

    public static JSONObject sendSms(String apikey, Map<String, String> temp, String mobile) throws IOException, ParseException {
        Map<String, String> params = new HashMap<>();
        params.put(YPConstants.APIKEY, apikey);
        params.put(YPConstants.TPL_ID, String.valueOf(YPConstants.TPL_ID_VALUE));
        params.put(YPConstants.TPL_VALUE, parse(temp));
        params.put(YPConstants.MOBILE, mobile);
        HttpClient httpClient = new HttpClient(YPConstants.URI_TPL_SEND_SMS, params);
        httpClient.post();
        JSONObject result = JSONObject.parseObject(httpClient.getContent());
        return result;
    }

    private static String parse(Map<String, String> temp) {
        StringBuffer tempBuffer = new StringBuffer(temp.size());
        try {
            for (Map.Entry<String, String> entry : temp.entrySet()) {
                tempBuffer.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.name()))
                    .append("=")
                    .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.name()))
                    .append("&");
            }
          return tempBuffer.deleteCharAt(tempBuffer.length() - 1).toString();
        }catch (UnsupportedEncodingException e) {
                e.printStackTrace();
        }
        return null;
    }

}
