package com.ly.storeserver.admin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.storeserver.admin.models.entity.Sysuser;
import com.ly.storeserver.admin.mapper.SysuserMapper;
import com.ly.storeserver.admin.models.request.LoginRequest;
import com.ly.storeserver.admin.models.request.RegisterRequest;
import com.ly.storeserver.admin.models.request.UserQueryRequest;
import com.ly.storeserver.admin.models.request.UserRequest;
import com.ly.storeserver.admin.models.response.LoginResponse;
import com.ly.storeserver.admin.models.response.UserResponse;
import com.ly.storeserver.admin.service.SysuserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ly.storeserver.common.annotation.MysqlSlave;
import com.ly.storeserver.common.bean.RPage;
import com.ly.storeserver.common.constant.YPConstants;
import com.ly.storeserver.common.enums.RStatus;
import com.ly.storeserver.exception.ServiceException;
import com.ly.storeserver.utils.CodeUtil;
import com.ly.storeserver.utils.JwtTokenUtil;
import com.ly.storeserver.utils.PasswordEncoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author ly
 * @since 2020-03-30 22:20
 */
@Service
@Slf4j
public class SysuserServiceImpl extends ServiceImpl<SysuserMapper, Sysuser> implements SysuserService {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SysuserMapper sysuserMapper;

    /**
     * 初始密码
     */
    public static String password = "123456";

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        String userMobile = loginRequest.getUserMobile();
        String password = loginRequest.getPassword();
        Sysuser sysuser = getOne(Wrappers.<Sysuser>lambdaQuery().eq(Sysuser::getUserPhone, userMobile));
        if (null == sysuser) throw new ServiceException("不存在此用户~", RStatus.FAIL);
        String sysPassword = sysuser.getPassword();
        if (!StringUtils.isBlank(sysPassword) && !passwordEncoder.matches(password, sysPassword)) {
            throw new ServiceException("密码错误~", RStatus.FAIL);
        }
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUsername(sysuser.getUserName());
        loginResponse.setAdminFlag(sysuser.getAdminFlag());
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("userMobile", sysuser.getUserPhone());
        String token = jwtTokenUtil.generateToken(String.valueOf(sysuser.getId()), userMap);
        loginResponse.setToken(token);
        return loginResponse;
    }

    @Override
    public void register(RegisterRequest registerRequest) {
        String confirmPassword = registerRequest.getConfirmPassword();
        String password = registerRequest.getPassword();
        if (!password.equals(confirmPassword)) throw new ServiceException("两次输入密码不一样~", RStatus.FAIL);
        String userMobile = registerRequest.getUserMobile();
        getOne(userMobile);
        Sysuser newSysuser = new Sysuser();
        newSysuser.setUserPhone(userMobile);
        newSysuser.setUserName(userMobile);
        newSysuser.setPassword(passwordEncoder.encode(password));
        newSysuser.setAdminFlag("普通员工");
        save(newSysuser);
    }

    @Override
    public boolean sendCode(String phone, String code) {
        CompletableFuture.runAsync(() -> {
            try {
                JSONObject jsonObject = CodeUtil.sendSms(YPConstants.APIKEY_VALUE, createSendTemp(code), phone);
                log.info("手机号{}，验证码{}，发送状态：{}", phone, code, jsonObject.getString("msg"));
            } catch (IOException | ParseException e) {
                e.printStackTrace();
                log.info("手机号{}，短信发送失败！", phone);
            }
        });
        return true;
    }

    @Override
    public void saveUser(UserRequest userRequest) {
        String userMobile = userRequest.getUserPhone();
        getOne(userMobile);
        Sysuser sysuser = new Sysuser();
        BeanUtils.copyProperties(userRequest, sysuser);
        sysuser.setPassword(passwordEncoder.encode(password));
        save(sysuser);
    }

    @Override
    @MysqlSlave
    public RPage<UserResponse> findUserList(UserQueryRequest userQueryRequest) {
        Integer pageSize = userQueryRequest.getPageSize();
        Integer pageNumber = userQueryRequest.getPageNumber();
        if (pageSize == null || pageSize <= 0) {
            pageSize = 10;
        }
        if (pageNumber == null || pageNumber <= 0) {
            pageNumber = 1;
        }
        IPage<Sysuser> page = new Page<>(pageNumber, pageSize);
        String userName = userQueryRequest.getUserName();
        String userPhone = userQueryRequest.getUserPhone();
        String address = userQueryRequest.getAddress();
        String sex = userQueryRequest.getSex();
        LambdaQueryWrapper<Sysuser> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(userName)) {
            queryWrapper.likeRight(Sysuser::getUserName, userName);
        }
        if (!StringUtils.isEmpty(userPhone)) {
            queryWrapper.likeRight(Sysuser::getUserPhone, userPhone);
        }
        if (!StringUtils.isEmpty(address)) {
            queryWrapper.likeRight(Sysuser::getUserAddress, address);
        }
        if (!StringUtils.isEmpty(sex)) {
            queryWrapper.eq(Sysuser::getUserSex, sex);
        }
        IPage<Sysuser> sysuserIPage = sysuserMapper.selectPage(page, queryWrapper);
        long total = sysuserIPage.getTotal();
        List<Sysuser> sysuserList = sysuserIPage.getRecords();
        List<UserResponse> userResponses = new ArrayList<>();
        RPage<UserResponse> rPage = new RPage<>();
        rPage.setTotal(total);
        if (!CollectionUtils.isEmpty(sysuserList)) {
            userResponses = sysuserList.stream()
                    .map(store -> getUserResponse(store))
                    .collect(Collectors.toList());
        }
        rPage.setData(userResponses);
        return rPage;
    }

    private UserResponse getUserResponse(Sysuser sysuser) {
        UserResponse userResponse = new UserResponse();
        BeanUtils.copyProperties(sysuser, userResponse);
        return userResponse;
    }

    @Override
    public void editUser(UserRequest userRequest) {
        Long id = userRequest.getId();
        Sysuser sysuser = sysuserMapper.selectById(id);
        if (sysuser == null) throw new ServiceException("用户不存在,更新失败~", RStatus.FAIL);
        BeanUtils.copyProperties(userRequest, sysuser);
        if (userRequest.getId() == null) {
            save(sysuser);
            return;
        }
        sysuserMapper.updateById(sysuser);
    }

    @Override
    public void delUser(Long userId) {
        if (userId == null) throw new ServiceException("用户主键不存在~", RStatus.FAIL);
        sysuserMapper.deleteById(userId);
    }

    private Sysuser getOne(String userMobile) {
        Wrapper<Sysuser> queryWrapper = Wrappers.<Sysuser>lambdaQuery().eq(Sysuser::getUserPhone, userMobile);
        Sysuser sysuser = sysuserMapper.selectOne(queryWrapper);
        if (null != sysuser) throw new ServiceException("用户已经存在~", RStatus.FAIL);
        return sysuser;
    }

    /**
     * 创建云片短信模板
     * @param code
     * @return
     */
    private Map<String, String> createSendTemp(String code) {
        Map<String, String> map = new HashMap<>();
        map.put("#code#", code);
        return map;
    }
}
