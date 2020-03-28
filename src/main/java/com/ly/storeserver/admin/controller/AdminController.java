package com.ly.storeserver.admin.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName AdminController
 * @Description: TODO
 * @Author ly
 * @Date 2020/3/28
 * @Version V1.0.0
 **/
@RestController
@RequestMapping("/admin/")
@Api(value = "管理员接口", tags = "管理员API")
public class AdminController {

    @PostMapping("login")
    @ApiOperation(value = "登录", notes = "登录接口")
    public String login() {
        return "登录成功";
    }

}
