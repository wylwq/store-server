package com.ly.storeserver.ext.open.request;

import lombok.Data;

/**
 * open-api基本的请求实体
 *
 * @author : wangyu
 * @since :  2020/12/25/025 11:11
 */
@Data
public class BaseRequest {

    /**
     * 实例id
     */
    private Long instanceId;

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 请求体
     */
    private String body;

}
