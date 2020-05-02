package com.ly.storeserver.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ly.storeserver.admin.models.entity.Store;
import com.ly.storeserver.admin.models.request.StoreQueryRequest;
import com.ly.storeserver.admin.models.request.StoreRequest;
import com.ly.storeserver.admin.models.response.StoreResponse;
import com.ly.storeserver.common.bean.RPage;

import java.util.List;

/**
 * @author ly
 * @since 2020-04-12 14:20
 */
public interface StoreService extends IService<Store> {

    /**
     * 添加商品库存
     * @param storeRequest
     * @param userMobile
     */
    void addStore(StoreRequest storeRequest, String userMobile);

    /**
     * 编辑商品信息
     * @param storeRequest
     * @param userMobile
     */
    void editStore(StoreRequest storeRequest, String userMobile);

    /**
     * 查询商品信息
     * @param storeQueryRequest
     */
    RPage<StoreResponse> queryStore(StoreQueryRequest storeQueryRequest);

    /**
     * 删除库存信息
     * @param ids
     */
    void delStore(Integer[] ids);

    /**
     * 通过id查询库存
     * @param id
     * @return
     */
    StoreResponse queryStoreById(Integer id);
}
