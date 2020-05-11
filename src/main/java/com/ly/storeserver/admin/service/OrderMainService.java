package com.ly.storeserver.admin.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ly.storeserver.admin.models.entity.OrderMain;
import com.ly.storeserver.admin.models.request.Cart;
import com.ly.storeserver.admin.models.request.OrderQueryRequest;
import com.ly.storeserver.admin.models.request.OrderRequest;
import com.ly.storeserver.admin.models.response.OrderItemResponse;
import com.ly.storeserver.admin.models.response.OrderMainResponse;
import com.ly.storeserver.common.bean.RPage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

/**
 * @author ly
 * @since 2020-04-26 21:41
 */
public interface OrderMainService extends IService<OrderMain> {

    /**
     * 订单预定
     * @param orderRequest
     */
    void commitOrder(OrderRequest orderRequest);

    /**
     * 查询订单列表
     * @param orderQueryRequest
     * @return
     */
    RPage<OrderMainResponse> queryOrderList(OrderQueryRequest orderQueryRequest);

    /**
     * 查询订单详情
     * @param orderItemId
     * @return
     */
    OrderItemResponse queryOrderItem(Long orderItemId);

    /**
     * 取消订单
     * @param orderId
     */
    void cancelOrder(Long orderId);

    /**
     * 删除订单
     * @param orderId
     */
    void delOrder(Long orderId);

    /**
     * 销量统计接口
     * @return
     */
    SortedMap<String, Long> statisticsOrder();

}
