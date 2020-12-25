package com.ly.storeserver.ext.open.controller;

import com.ly.storeserver.common.annotation.Ratelimiter;
import com.ly.storeserver.common.annotation.Token;
import com.ly.storeserver.ext.open.exception.ProxyApiException;
import com.ly.storeserver.ext.open.handler.MethodProxyFactory;
import com.ly.storeserver.ext.open.request.BaseRequest;
import com.ly.storeserver.ext.open.response.BaseResponse;
import com.ly.storeserver.ext.open.response.ReturnCode;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;

/**
 * open-api开放接口控制层
 *
 * @author : wangyu
 * @since :  2020/12/25/025 13:44
 */
@RestController("/open-service/v1")
public class OpenApiController {

    @Autowired
    private MethodProxyFactory methodProxyFactory;

    @ApiOperation(value = "外部系统对接开发open-api")
    @PostMapping("/proxy-service/{serviceName}")
    @Token()
    @Ratelimiter(limit = 10, timeout = 1)
    public BaseResponse proxyServiceV1(@PathVariable(name = "serviceName") String serviceName,
                                       HttpServletRequest request,
                                       @RequestBody BaseRequest baseRequest) {
        if (StringUtils.isEmpty(baseRequest.getServiceName())
                || StringUtils.isEmpty(baseRequest.getBody())
                || baseRequest.getInstanceId() == null) {
            return new BaseResponse(ReturnCode.PARAM_NULL);
        }
        try {
            return methodProxyFactory.invokeProxyService(baseRequest);
        } catch (ProxyApiException e) {
            return new BaseResponse(e);
        } catch (IllegalAccessException | InvocationTargetException e) {
            return new BaseResponse(ReturnCode.AUTH_FAIL);
        }

    }

}
