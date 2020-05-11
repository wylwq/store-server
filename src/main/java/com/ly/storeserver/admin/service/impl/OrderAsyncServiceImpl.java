package com.ly.storeserver.admin.service.impl;

import com.ly.storeserver.admin.mapper.OrderItemMapper;
import com.ly.storeserver.admin.mapper.OrderMainMapper;
import com.ly.storeserver.admin.models.entity.OrderItem;
import com.ly.storeserver.admin.models.entity.OrderMain;
import com.ly.storeserver.admin.models.entity.Store;
import com.ly.storeserver.admin.models.request.Cart;
import com.ly.storeserver.admin.models.request.OrderRequest;
import com.ly.storeserver.admin.models.response.StoreResponse;
import com.ly.storeserver.admin.service.OrderAsyncService;
import com.ly.storeserver.admin.service.StoreService;
import com.ly.storeserver.common.enums.OrderEnums;
import com.ly.storeserver.common.enums.PayEnums;
import com.ly.storeserver.common.enums.RStatus;
import com.ly.storeserver.exception.ServiceException;
import com.ly.storeserver.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description:
 * @Author ly
 * @Date 2020/5/10 12:10
 * @Version V1.0.0
 **/
@Component
public class OrderAsyncServiceImpl implements OrderAsyncService {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private OrderMainMapper orderMainMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private StoreService storeService;

    /**
     * 处理订单提交
     * @param orderRequest
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void postCommit(OrderRequest orderRequest) throws ServiceException{
        String userPhone = orderRequest.getUserPhone();
        Integer payType = orderRequest.getPayType();
        String orderAddress = orderRequest.getOrderAddress();
        Long orderAmount = orderRequest.getOrderRealAmount();
        String operation = orderRequest.getOperation();
        List<Cart> cartList = orderRequest.getCartList();
        OrderMain orderMain = new OrderMain();
        orderMain.setOrderNo(String.valueOf(idWorker.nextId()));
        orderMain.setOperation(operation);
        orderMain.setOrderAddress(orderAddress);
        orderMain.setOrderAmount(orderAmount);
        orderMain.setOrderStatus(OrderEnums.NO_SEND.getStatus());
        orderMain.setPayStatus(PayEnums.NO_PAY.getStatus());
        orderMain.setPayType(payType);
        orderMain.setUserPhone(userPhone);
        orderMainMapper.insert(orderMain);
        Long id = orderMain.getId();
        saveOrderItem(id.intValue(), cartList);
    }

    /**
     * 这里可以不用synchronized，因为数据库我使用了乐观锁(version)
     * @param orderMainId
     * @param cartList
     */
    private void saveOrderItem(Integer orderMainId, List<Cart> cartList){
        synchronized (OrderMainServiceImpl.class) {
            cartList.forEach(cart -> {
                Integer goodsNum = cart.getGoodsNum();
                Long goodsId = cart.getGoodsId();
                StoreResponse storeResponse = storeService.queryStoreById(goodsId.intValue());
                Integer storeNum = storeResponse.getStoreNum();
                Integer resultNum = storeNum -  goodsNum;
                if (resultNum < 0) throw new ServiceException("库存不足预定失败,当前库存"+storeNum+"用户需要库存"+goodsNum+"~", RStatus.FAIL);
                Store store = new Store();
                store.setId(storeResponse.getId());
                store.setStoreNum(resultNum);
                storeService.updateById(store);
                OrderItem orderItem = new OrderItem();
                orderItem.setGoodsId(goodsId);
                orderItem.setGoodsName(cart.getGoodsName());
                orderItem.setGoodsPrice(cart.getGoodsPrice());
                orderItem.setOrderItemNo(String.valueOf(idWorker.nextId()));
                orderItem.setOrderMainId(Long.valueOf(orderMainId));
                orderItem.setGoodsNum(goodsNum);
                orderItem.setGoodsTotal(cart.getGoodsTotal());
                orderItem.setOrderItemStatus(OrderEnums.NO_SEND.getStatus());
                orderItemMapper.insert(orderItem);
            });
        }
    }
}
