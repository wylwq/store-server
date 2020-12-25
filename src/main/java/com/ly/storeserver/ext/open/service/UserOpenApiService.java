package com.ly.storeserver.ext.open.service;

import com.ly.storeserver.common.annotation.ApiProxyMethod;
import com.ly.storeserver.common.annotation.ApiProxyService;
import com.ly.storeserver.ext.open.request.SyncEmployRequest;
import com.ly.storeserver.ext.open.response.SyncEmployResponse;

/**
 * 用户开放接口
 *
 * @author : wangyu
 * @since :  2020/12/25/025 15:30
 */
@ApiProxyService
public class UserOpenApiService {

    @ApiProxyMethod(serviceName = "syncEmploy", requestType = SyncEmployRequest.class)
    public SyncEmployResponse syncEmployService(Long merchantId, SyncEmployRequest request) {
        return new SyncEmployResponse();
    }
}
