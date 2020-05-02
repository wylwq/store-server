package com.ly.storeserver.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ly.storeserver.admin.mapper.OrderItemMapper;
import com.ly.storeserver.admin.models.entity.OrderItem;
import com.ly.storeserver.admin.service.OrderItemService;
import org.springframework.stereotype.Service;

/**
 * @author ly
 * @since 2020-04-26 21:46
 */
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements OrderItemService {

}
