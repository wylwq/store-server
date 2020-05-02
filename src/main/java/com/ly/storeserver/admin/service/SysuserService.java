package com.ly.storeserver.admin.service;

import com.ly.storeserver.admin.models.entity.Sysuser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ly.storeserver.admin.models.request.LoginRequest;
import com.ly.storeserver.admin.models.request.RegisterRequest;
import com.ly.storeserver.admin.models.request.UserQueryRequest;
import com.ly.storeserver.admin.models.request.UserRequest;
import com.ly.storeserver.admin.models.response.LoginResponse;
import com.ly.storeserver.admin.models.response.UserResponse;
import com.ly.storeserver.common.bean.RPage;

/**
 * @author ly
 * @since 2020-03-30 22:20
 */
public interface SysuserService extends IService<Sysuser> {

    /**
     * 登录接口
     * @param loginRequest
     */
    LoginResponse login(LoginRequest loginRequest);

    /**
     * 注册接口
     * @param registerRequest
     */
    void register(RegisterRequest registerRequest);

    /**
     * 发送手机短信验证码
     * @param phone
     * @return
     */
    boolean sendCode(String phone, String code);

    /**
     * 添加用户接口
     * @param userRequest
     */
    void saveUser(UserRequest userRequest);

    /**
     * 查询用户列表
     * @param userQueryRequest
     * @return
     */
    RPage<UserResponse> userList(UserQueryRequest userQueryRequest);

    /**
     * 编辑用户列表
     * @param userRequest
     */
    void editUser(UserRequest userRequest);

    /**
     * 删除用户
     * @param userId
     */
    void delUser(Long userId);
}
