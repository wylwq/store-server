package com.ly.storeserver.admin.service;

import com.ly.storeserver.admin.models.request.OrderRequest;
import com.ly.storeserver.exception.ServiceException;

/**
 * @Description:
 * @Author ly
 * @Date 2020/5/10 12:09
 * @Version V1.0.0
 **/
public interface OrderAsyncService {

    void postCommit(OrderRequest orderRequest) throws ServiceException;
}
