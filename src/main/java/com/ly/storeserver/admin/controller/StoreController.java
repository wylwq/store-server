package com.ly.storeserver.admin.controller;


import com.ly.storeserver.admin.models.request.StoreQueryRequest;
import com.ly.storeserver.admin.models.request.StoreRequest;
import com.ly.storeserver.admin.models.response.StoreResponse;
import com.ly.storeserver.admin.service.StoreService;
import com.ly.storeserver.common.bean.R;
import com.ly.storeserver.common.bean.RPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import springfox.documentation.annotations.ApiIgnore;

/**
 * @author ly
 * @since 2020-04-12 14:20
 */
@RestController
@RequestMapping("/store/")
@Api(value = "库存API", tags = "库存API")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @ApiOperation(value = "添加库存", notes = "添加库存接口")
    @PostMapping(value = "addStore")
    public R addStore(@RequestBody @Validated StoreRequest storeRequest,
                      @ApiIgnore @RequestAttribute(value = "userMobile") String userMobile) {
        storeService.addStore(storeRequest, userMobile);
        return new R<>("添加商品库存成功~");
    }

    @ApiOperation(value = "修改库存", notes = "修改库存接口")
    @PostMapping(value = "editStore")
    public R editStore(@RequestBody StoreRequest storeRequest,
                      @ApiIgnore @RequestAttribute(value = "userMobile") String userMobile) {
        storeService.editStore(storeRequest, userMobile);
        return new R<>("修改商品库存成功~");
    }

    @ApiOperation(value = "查询库存", notes = "查询库存接口")
    @PostMapping(value = "queryStore")
    public RPage<StoreResponse> queryStore(@RequestBody StoreQueryRequest storeQueryRequest) {
        RPage<StoreResponse> rPage = storeService.queryStore(storeQueryRequest);
        return rPage;
    }

    @ApiOperation(value = "通过id查询库存", notes = "通过id查询库存接口")
    @GetMapping(value = "queryStoreById")
    public R queryStoreById(@RequestParam(value = "id") Integer id) {
        StoreResponse storeResponse = storeService.queryStoreById(id);
        return new R<>("查询成功", storeResponse);
    }

    @ApiOperation(value = "删除库存", notes = "删除库存接口")
    @GetMapping(value = "delStore")
    public R delStore(@RequestParam(value = "ids") Integer[] ids) {
        storeService.delStore(ids);
        return new R<>("删除商品库存成功~");
    }

}

