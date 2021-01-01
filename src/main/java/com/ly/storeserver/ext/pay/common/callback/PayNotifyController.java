package com.ly.storeserver.ext.pay.common.callback;

import com.alibaba.fastjson.JSON;
import com.ly.storeserver.common.annotation.Token;
import com.ly.storeserver.ext.pay.common.constant.ServiceCode;
import com.ly.storeserver.ext.pay.common.constant.SupplierCode;
import com.ly.storeserver.ext.pay.common.factory.PayNotifyAdapter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 支付统一通知接口
 *
 * @author : wangyu
 * @since :  2020/12/31/031 14:55
 */
@RestController
@RequestMapping("/pay/notify/v1")
@Slf4j
@ApiIgnore
public class PayNotifyController {

    @Autowired
    private PayNotifyAdapter payNotifyAdapter;

    /**
     * 支付统一回调入口
     *
     * @param supplierCode 供应商名称
     * @param type 回调类型
     * @param body 回调参数，非必须
     * @return 回调响应
     */
    @RequestMapping("/{supplierCode}/{type}")
    @Token
    public String payNotify(@PathVariable(name = "supplierCode") String supplierCode,
                            @PathVariable(name = "type") String type,
                            @RequestBody(required = false) String body,
                            HttpServletRequest request) {
        SupplierCode code = SupplierCode.getByCode(supplierCode);
        if (code == null) {
            return null;
        }
        ServiceCode serviceCode = ServiceCode.getByCode(type);
        if (serviceCode == null) {
            return null;
        }
        Map<String, String> requestBody = converterReq(request, body);
        log.info("远程支付通知结果如下：{}", JSON.toJSONString(requestBody));
        return payNotifyAdapter.handler(requestBody, code, serviceCode);
    }

    /**
     * 将远程通知结果转换为Map
     * @param request
     * @param body
     * @return
     */
    private Map<String, String> converterReq(HttpServletRequest request, String body) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, String> paramMap = new HashMap<>();
        if (parameterMap != null && parameterMap.size() > 0) {
            Set<String> keySet = parameterMap.keySet();
            for (String key : keySet) {
                String value = parameterMap.get(key)[0];
                paramMap.put(key, value);
            }
            return paramMap;
        }
        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                String requestBody = StreamUtils.copyToString(inputStream, Charset.forName("UTF-8"));
                //xml to map
                return paramMap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!StringUtils.isEmpty(body) && body.startsWith("{") && body.endsWith("}")) {
            String[] maps = body.split("&");
            for (String entity : maps) {
                String[] split = entity.split("=");
                if (split.length == 2) {
                    paramMap.put(split[0], split[1]);
                }
            }
            return paramMap;
        }
        return new HashMap<>();
    }

}
