package com.ly.storeserver.admin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ly.storeserver.admin.mapper.OrderItemMapper;
import com.ly.storeserver.admin.mapper.OrderMainMapper;
import com.ly.storeserver.admin.models.entity.OrderItem;
import com.ly.storeserver.admin.models.entity.OrderMain;
import com.ly.storeserver.admin.models.request.Cart;
import com.ly.storeserver.admin.models.request.OrderQueryRequest;
import com.ly.storeserver.admin.models.request.OrderRequest;
import com.ly.storeserver.admin.models.response.OrderItemResponse;
import com.ly.storeserver.admin.models.response.OrderMainResponse;
import com.ly.storeserver.admin.models.response.OrderStatisticResponse;
import com.ly.storeserver.admin.service.OrderMainService;
import com.ly.storeserver.common.bean.RPage;
import com.ly.storeserver.common.enums.OrderEnums;
import com.ly.storeserver.common.enums.PayEnums;
import com.ly.storeserver.common.enums.RStatus;
import com.ly.storeserver.exception.ServiceException;
import com.ly.storeserver.utils.IdWorker;
import com.ly.storeserver.utils.ParamValidUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ly
 * @since 2020-04-26 21:41
 */
@Service
public class OrderMainServiceImpl extends ServiceImpl<OrderMainMapper, OrderMain> implements OrderMainService {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private ParamValidUtil paramValidUtil;

    @Autowired
    private OrderMainMapper orderMainMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void commitOrder(OrderRequest orderRequest) {
        paramValidUtil.valid(orderRequest);
        String userPhone = orderRequest.getUserPhone();
        Integer payType = orderRequest.getPayType();
        String orderAddress = orderRequest.getOrderAddress();
        Long orderAmount = orderRequest.getOrderAmount();
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
        int orderMainId = orderMainMapper.insert(orderMain);
        saveOrderItem(orderMainId, cartList);

    }

    private void saveOrderItem(Integer orderMainId, List<Cart> cartList) {
        cartList.forEach(cart -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setGoodsId(cart.getGoodsId());
            orderItem.setGoodsName(cart.getGoodsName());
            orderItem.setGoodsPrice(cart.getGoodsPrice());
            orderItem.setOrderItemNo(String.valueOf(idWorker.nextId()));
            orderItem.setOrderMainId(Long.valueOf(orderMainId));
            orderItem.setGoodsNum(cart.getGoodsNum());
            orderItem.setGoodsTotal(cart.getGoodsTotal());
            orderItem.setOrderItemStatus(OrderEnums.NO_SEND.getStatus());
            orderItemMapper.insert(orderItem);
        });

    }

    @Override
    public RPage<OrderMainResponse> queryOrderList(OrderQueryRequest orderQueryRequest) {
        Integer pageSize = orderQueryRequest.getPageSize();
        Integer pageNumber = orderQueryRequest.getPageNumber();
        String createTime = orderQueryRequest.getCreateTime();
        String orderNo = orderQueryRequest.getOrderNo();
        Integer orderStatus = orderQueryRequest.getOrderStatus();
        if (pageSize == null || pageSize <= 0) {
            pageSize = 10;
        }
        if (pageNumber == null || pageNumber <= 0) {
            pageNumber = 1;
        }
        IPage<OrderMain> page = new Page<>(pageNumber, pageSize);
        LambdaQueryWrapper<OrderMain> wrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(orderNo)) {
            wrapper.likeRight(OrderMain::getOrderNo, orderNo);
        }
        if (createTime != null) {
            wrapper.ge(OrderMain::getCreateTime, createTime);
        }
        if (orderStatus != null) {
            wrapper.eq(OrderMain::getOrderStatus, orderStatus);
        }
        IPage<OrderMain> orderMainIPage = orderMainMapper.selectPage(page, wrapper);
        long total = orderMainIPage.getTotal();
        List<OrderMain> orderMainList = orderMainIPage.getRecords();
        List<OrderMainResponse> orderMainResponses = new ArrayList<>();
        RPage<OrderMainResponse> rPage = new RPage<>();
        rPage.setTotal(total);
        if (!CollectionUtils.isEmpty(orderMainList)) {
            orderMainResponses = orderMainList.stream()
                    .map(orderMain -> getOrderMainResponse(orderMain))
                    .collect(Collectors.toList());
        }
        rPage.setData(orderMainResponses);
        return rPage;
    }

    private OrderMainResponse getOrderMainResponse(OrderMain orderMain) {
        OrderMainResponse orderMainResponse = new OrderMainResponse();
        BeanUtils.copyProperties(orderMain, orderMainResponse);
        return orderMainResponse;
    }

    @Override
    public OrderItemResponse queryOrderItem(Long orderItemId) {
        OrderMain orderMain = orderMainMapper.selectById(orderItemId);
        if (orderMain == null) throw new ServiceException( "订单详情不存在", RStatus.FAIL);
        LambdaQueryWrapper<OrderItem> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(OrderItem::getOrderMainId, orderMain.getId());
        OrderItem orderItem = orderItemMapper.selectOne(wrapper);
        if (orderItem == null) throw new ServiceException("订单详情不存在", RStatus.FAIL);
        OrderItemResponse orderItemResponse = getOrderItemResponse(orderItem, orderMain.getOrderAddress());
        return orderItemResponse;
    }

    private OrderItemResponse getOrderItemResponse(OrderItem orderItem, String orderAddress) {
        OrderItemResponse orderItemResponse = new OrderItemResponse();
        BeanUtils.copyProperties(orderItem, orderItemResponse);
        orderItemResponse.setOrderAddress(orderAddress);
        return orderItemResponse;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void cancelOrder(Long orderId) {
        if (orderId == null || orderId <= 0) throw new ServiceException("订单号异常~");
        OrderMain orderMain = orderMainMapper.selectById(orderId);
        if (orderMain == null || orderMain.getOrderStatus().equals(OrderEnums.CANCEL.getStatus())) {
            throw new ServiceException("订单取消失败~");
        }
        orderMain.setOrderStatus(OrderEnums.CANCEL.getStatus());
        orderMainMapper.updateById(orderMain);
        LambdaQueryWrapper<OrderItem> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(OrderItem::getOrderMainId, orderId);
        List<OrderItem> orderItems = orderItemMapper.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(orderItems)) {
            orderItems.forEach(orderItem -> {
                orderItem.setOrderItemStatus(OrderEnums.CANCEL.getStatus());
                orderItemMapper.updateById(orderItem);
            });
        }

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void delOrder(Long orderId) {
        if (orderId == null || orderId <= 0) throw new ServiceException("订单号异常~");
        OrderMain orderMain = orderMainMapper.selectById(orderId);
        if (Objects.equals(orderMain.getDeleted(), OrderEnums.DELETE.getStatus())) throw new ServiceException("订单删除失败~");
        orderMain.setDeleted(OrderEnums.DELETE.getStatus());
        orderMainMapper.deleteById(orderId);
        LambdaQueryWrapper<OrderItem> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(OrderItem::getOrderMainId, orderId);
        List<OrderItem> orderItems = orderItemMapper.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(orderItems)) {
            List<Long> collect = orderItems.stream().map(orderItem -> orderItem.getId()).collect(Collectors.toList());
            orderItemMapper.deleteBatchIds(collect);
        }
    }

    @Override
    public Map<String, Long> statisticsOrder() {
        Map<String, Long> map = new HashMap<>();
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (int i = 0; i < 7; i++) {
            LocalDate minLocalDate = localDate.minusDays(i);
            map.put(dateTimeFormatter.format(minLocalDate), 0L);
        }
        List<OrderStatisticResponse> orderStatisticResponses = orderMainMapper.statisticsOrder();
        orderStatisticResponses.forEach(orderStatisticResponse -> {
            if (map.containsKey(orderStatisticResponse.getDate())) {
                map.put(orderStatisticResponse.getDate(), orderStatisticResponse.getNum());
            }
        });
        orderStatisticResponses.clear();
        return map;
    }
}
