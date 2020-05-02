package com.ly.storeserver.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ly.storeserver.admin.models.entity.OrderMain;
import com.ly.storeserver.admin.models.response.OrderStatisticResponse;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author ly
 * @since 2020-04-26 21:41
 */
public interface OrderMainMapper extends BaseMapper<OrderMain> {

    @Select("SELECT DATE_FORMAT(create_time,'%Y-%m-%d') date, COUNT(id) num FROM s_order_main WHERE pay_status = 1 GROUP BY DATE_FORMAT(create_time,'%Y-%m-%d')")
    List<OrderStatisticResponse> statisticsOrder();

}
