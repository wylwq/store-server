package com.ly.storeserver.admin.controller;

import com.ly.storeserver.admin.models.request.LoginRequest;
import com.ly.storeserver.admin.models.request.RegisterRequest;
import com.ly.storeserver.admin.models.request.UserQueryRequest;
import com.ly.storeserver.admin.models.request.UserRequest;
import com.ly.storeserver.admin.models.response.LoginResponse;
import com.ly.storeserver.admin.models.response.UserResponse;
import com.ly.storeserver.admin.service.SysuserService;
import com.ly.storeserver.common.annotation.Ratelimiter;
import com.ly.storeserver.common.annotation.Token;
import com.ly.storeserver.common.bean.RPage;
import com.ly.storeserver.common.controller.BaseController;
import com.ly.storeserver.common.bean.R;
import com.ly.storeserver.common.enums.RStatus;
import com.ly.storeserver.config.DbConfig;
import com.ly.storeserver.config.DynamicDataSource;
import com.ly.storeserver.exception.ServiceException;
import com.ly.storeserver.utils.CodeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @Author ly
 * @Date 2020/3/28
 * @Version V1.0.0
 **/
@RestController
@RequestMapping("/admin/")
@Api(value = "管理员API", tags = "管理员API")
public class AdminController extends BaseController {

    @Autowired
    private SysuserService sysuserService;

    @PostMapping("login")
    @ApiOperation(value = "登录", notes = "登录接口")
    @Token
    public R login(@RequestBody LoginRequest loginRequest) {
        DbConfig.getDb();
        LoginResponse loginResponse = sysuserService.login(loginRequest);
        return new R<>(RStatus.SUCCESS, loginResponse);
    }

    @PostMapping("register")
    @ApiOperation(value = "注册", notes = "注册接口")
    @Token
    public R register(@RequestBody @Valid RegisterRequest registerRequest) {
        String code = registerRequest.getCode();
        String userMobile = registerRequest.getUserMobile();
        String oldCode = getCode(userMobile);
        if (StringUtils.isBlank(oldCode) || !oldCode.equals(code)) throw new ServiceException("验证码错误~", RStatus.FAIL);
        sysuserService.register(registerRequest);
        return new R<>("注册成功~");
    }

    @GetMapping("sendCode")
    @ApiOperation(value = "发送手机验证码", notes = "发送手机验证码接口")
    @Token
    @Ratelimiter(limit = 0.02, timeout = 50)
    public R sendCode(@RequestParam(value = "phone") @NotNull(message = "手机号不能为空") String phone) {
        String code = CodeUtil.code();
        boolean sendFlag = putCode(phone, code);
        if (!sendFlag) return new R<>(RStatus.SEDN_CODE);
        sysuserService.sendCode(phone, code);
        return new R<>("验证码发送成功~");
    }

    @PostMapping("saveUser")
    @ApiOperation(value = "添加用户", notes = "添加用户接口")
    public R saveUser(@RequestBody @Validated UserRequest userRequest) {
        sysuserService.saveUser(userRequest);
        return new R<>("添加用户成功");
    }

    @PostMapping("userList")
    @ApiOperation(value = "获取用户列表", notes = "获取用户列表接口")
    public RPage userList(@RequestBody UserQueryRequest userQueryRequest) {
        RPage<UserResponse> sysuserIPage = sysuserService.userList(userQueryRequest);
        return sysuserIPage;
    }

    @PostMapping("editUser")
    @ApiOperation(value = "编辑用户", notes = "编辑用户接口")
    public R editUser(@RequestBody UserRequest userRequest) {
        sysuserService.editUser(userRequest);
        return new R<>("编辑用户成功");
    }

    @GetMapping("delUser")
    @ApiOperation(value = "删除用户", notes = "删除用户接口")
    public R delUser(@RequestParam Long userId) {
        sysuserService.delUser(userId);
        return new R<>("删除用户成功");
    }

}
