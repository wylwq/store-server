package com.ly.storeserver.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ly.storeserver.admin.mapper.StoreMapper;
import com.ly.storeserver.admin.models.entity.Store;
import com.ly.storeserver.admin.models.request.StoreQueryRequest;
import com.ly.storeserver.admin.models.request.StoreRequest;
import com.ly.storeserver.admin.models.response.StoreResponse;
import com.ly.storeserver.admin.service.StoreService;
import com.ly.storeserver.common.bean.RPage;
import com.ly.storeserver.common.enums.RStatus;
import com.ly.storeserver.exception.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ly
 * @since 2020-04-12 14:20
 */
@Service
public class StoreServiceImpl extends ServiceImpl<StoreMapper, Store> implements StoreService {

    @Autowired
    private StoreMapper storeMapper;

    @Override
    public void addStore(StoreRequest storeRequest, String userMobile) {
        Store store = new Store();
        BeanUtils.copyProperties(storeRequest, store);
        store.setOperation(userMobile);
        store.setStoreFee(store.getStoreFee() * 100);
        storeMapper.insert(store);
    }

    @Override
    public void editStore(StoreRequest storeRequest, String userMobile) {
        Integer id = storeRequest.getId();
        Store store = storeMapper.selectById(id);
        if (store == null) throw new ServiceException("商品不存在,更新失败~", RStatus.FAIL);
        BeanUtils.copyProperties(storeRequest, store);
        store.setId(id);
        store.setOperation(userMobile);
        saveOrUpdate(store);
    }

    @Override
    public RPage<StoreResponse> queryStore(StoreQueryRequest storeQueryRequest) {
        Integer pageSize = storeQueryRequest.getPageSize();
        Integer pageNumber = storeQueryRequest.getPageNumber();
        String storeParent = storeQueryRequest.getStoreParent();
        Integer storeStatus = storeQueryRequest.getStoreStatus();
        Date createTime = storeQueryRequest.getCreateTime();
        String keyWord = storeQueryRequest.getKeyWord();
        if (pageSize == null || pageSize <= 0) {
            pageSize = 10;
        }
        if (pageNumber == null || pageNumber <= 0) {
            pageNumber = 1;
        }
        IPage<Store> page = new Page<>(pageNumber, pageSize);
        LambdaQueryWrapper<Store> wrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(keyWord)) {
            wrapper.likeRight(Store::getStoreName, keyWord);
        }
        if (createTime != null) {
            wrapper.ge(Store::getCreateTime, createTime);
        }
        if (!StringUtils.isEmpty(storeParent)) {
            wrapper.eq(Store::getParentName, storeParent);
        }
        if (storeStatus != null) {
            wrapper.eq(Store::getStoreStatus, storeStatus);
        }
        IPage<Store> storeIPage = storeMapper.selectPage(page, wrapper);
        long total = storeIPage.getTotal();
        List<Store> storeList = storeIPage.getRecords();
        List<StoreResponse> storeResponses = new ArrayList<>();
        RPage<StoreResponse> rPage = new RPage<>();
        rPage.setTotal(total);
        if (!CollectionUtils.isEmpty(storeList)) {
            storeResponses = storeList.stream()
                    .map(store -> getStoreResponse(store))
                    .collect(Collectors.toList());
        }
        rPage.setData(storeResponses);
        return rPage;
    }

    @Override
    public void delStore(Integer[] ids) {
        if (ids == null || ids.length == 0) throw new ServiceException("主键不能为空~", RStatus.FAIL);
        storeMapper.deleteBatchIds(CollectionUtils.arrayToList(ids));
    }

    @Override
    public StoreResponse queryStoreById(Integer id) {
        Store store = storeMapper.selectById(id);
        if (store == null) throw new ServiceException("商品不存在~", RStatus.FAIL);
        return getStoreResponse(store);
    }

    private StoreResponse getStoreResponse(Store store) {
        StoreResponse storeResponse = new StoreResponse();
        storeResponse.setId(store.getId());
        storeResponse.setCreateTime(store.getCreateTime());
        storeResponse.setStoreFee(store.getStoreFee());
        storeResponse.setStoreName(store.getStoreName());
        storeResponse.setStoreParam(store.getStoreParam());
        storeResponse.setStoreStatus(store.getStoreStatus());
        storeResponse.setStoreNum(store.getStoreNum());
        return storeResponse;
    }
}
