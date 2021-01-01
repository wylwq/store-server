package com.ly.storeserver.admin.controller;


import com.ly.storeserver.admin.models.request.CustomQueryRequest;
import com.ly.storeserver.admin.models.request.CustomRequest;
import com.ly.storeserver.admin.models.response.CustomResponse;
import com.ly.storeserver.admin.service.CustomService;
import com.ly.storeserver.common.bean.R;
import com.ly.storeserver.common.bean.RPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author ly
 * @since 2020-05-06 22:30
 */
@RestController
@RequestMapping("/custom/")
@Api(value = "客户API", tags = "客户API")
public class CustomController {

    @Autowired
    private CustomService customService;

    @PostMapping("saveCustom")
    @ApiOperation(value = "添加客户", notes = "添加客户接口")
    public R saveCustom(@RequestBody @Validated CustomRequest customRequest) {
        customService.saveCustom(customRequest);
        return new R<>("保存客户信息成功");
    }

    @PostMapping("customList")
    @ApiOperation(value = "获取客户列表", notes = "获取客户列表接口")
    public RPage customList(@RequestBody CustomQueryRequest customQueryRequest) {
        RPage<CustomResponse> customIPage = customService.customList(customQueryRequest);
        return customIPage;
    }

    @GetMapping("findById")
    @ApiOperation(value = "查询客户", notes = "查询客户接口")
    public R editCustom(@RequestParam Long id) {
        return new R<>(customService.editCustom(id));
    }

    @GetMapping("findOneCustom")
    @ApiOperation(value = "根据手机号查询客户", notes = "根据手机号查询客户接口")
    public R findOneCustom(@RequestParam String cusPhone) {
        return new R<>(customService.findOneCustom(cusPhone));
    }

    @GetMapping("delCustom")
    @ApiOperation(value = "删除客户", notes = "删除客户接口")
    public R delCustom(@RequestParam Long id) {
        customService.delCustom(id);
        return new R<>("删除客户成功");
    }


}

