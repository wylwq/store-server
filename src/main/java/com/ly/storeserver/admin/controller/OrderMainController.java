package com.ly.storeserver.admin.controller;


import com.ly.storeserver.admin.models.request.OrderQueryRequest;
import com.ly.storeserver.admin.models.request.OrderRequest;
import com.ly.storeserver.admin.models.response.OrderItemResponse;
import com.ly.storeserver.admin.models.response.OrderMainResponse;
import com.ly.storeserver.admin.service.OrderMainService;
import com.ly.storeserver.common.bean.R;
import com.ly.storeserver.common.bean.RPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author ly
 * @since 2020-04-26 21:41
 */
@RestController
@RequestMapping("/order/")
@Api(value = "主订单API", tags = "主订单API")
public class OrderMainController {

    @Autowired
    private OrderMainService orderMainService;

    @ApiOperation(value = "订单预定", notes = "订单预定接口")
    @PostMapping("commitOrder")
    public R commitOrder(@RequestBody OrderRequest orderRequest) {
        orderMainService.commitOrder(orderRequest);
        return new R<>("下单成功");
    }

    @ApiOperation(value = "查询订单列表接口", notes = "查询订单列表接口")
    @PostMapping("orderList")
    public RPage orderList(@RequestBody OrderQueryRequest orderQueryRequest) {
        RPage<OrderMainResponse> orderMainResponseRPage = orderMainService.queryOrderList(orderQueryRequest);
        return orderMainResponseRPage;
    }

    @ApiOperation(value = "查询订单详情接口", notes = "查询订单详情接口")
    @GetMapping("queryOrderItem")
    public R queryOrderItem(@RequestParam Long orderItemId) {
        OrderItemResponse orderItemResponse = orderMainService.queryOrderItem(orderItemId);
        return new R<>(orderItemResponse);
    }

    @ApiOperation(value = "取消订单接口", notes = "取消订单接口")
    @GetMapping("cancelOrder")
    public R cancelOrder(@RequestParam Long orderId) {
        orderMainService.cancelOrder(orderId);
        return new R<>("取消订单成功");
    }

    @ApiOperation(value = "删除订单接口", notes = "删除订单接口")
    @GetMapping("delOrder")
    public R delOrder(@RequestParam Long orderId) {
        orderMainService.delOrder(orderId);
        return new R<>("删除订单成功");
    }

    @ApiOperation(value = "订单销量接口", notes = "销量统计接口")
    @GetMapping("statisticsOrder")
    public R statisticsOrder() {
        return new R(orderMainService.statisticsOrder());
    }

}

