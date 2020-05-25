package com.ly.storeserver.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ly.storeserver.admin.mapper.CustomMapper;
import com.ly.storeserver.admin.models.entity.Custom;
import com.ly.storeserver.admin.models.request.CustomQueryRequest;
import com.ly.storeserver.admin.models.request.CustomRequest;
import com.ly.storeserver.admin.models.response.CustomResponse;
import com.ly.storeserver.admin.service.CustomService;
import com.ly.storeserver.common.bean.RPage;
import com.ly.storeserver.common.enums.RStatus;
import com.ly.storeserver.exception.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ly
 * @since 2020-05-06 22:30
 */
@Service
public class CustomServiceImpl extends ServiceImpl<CustomMapper, Custom> implements CustomService {

    @Autowired
    private CustomMapper customMapper;

    @Override
    public void saveCustom(CustomRequest customRequest) {
        String cusPhone = customRequest.getCusPhone();
        LambdaQueryWrapper<Custom> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Custom::getCusPhone, cusPhone);
        Custom custom = getOne(wrapper);
        if (custom != null && customRequest.getId() == null) throw new ServiceException("该用户已经存在添加失败~", RStatus.FAIL);
        if (custom == null) custom = new Custom();
        BeanUtils.copyProperties(customRequest, custom);
        if (custom.getId() == null) {
            customMapper.insert(custom);
            return;
        }
        customMapper.updateById(custom);

    }

    @Override
    public RPage<CustomResponse> customList(CustomQueryRequest customQueryRequest) {
        Integer pageSize = customQueryRequest.getPageSize();
        Integer pageNumber = customQueryRequest.getPageNumber();
        if (pageSize == null || pageSize <= 0) {
            pageSize = 10;
        }
        if (pageNumber == null || pageNumber <= 0) {
            pageNumber = 1;
        }
        IPage<Custom> page = new Page<>(pageNumber, pageSize);
        String cusName = customQueryRequest.getCusName();
        String cusPhone = customQueryRequest.getCusPhone();
        Integer cusMembers = customQueryRequest.getCusMembers();
        LambdaQueryWrapper<Custom> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(cusName)) {
            queryWrapper.likeRight(Custom::getCusName, cusName);
        }
        if (!StringUtils.isEmpty(cusPhone)) {
            queryWrapper.likeRight(Custom::getCusPhone, cusPhone);
        }
        if (cusMembers != null) {
            queryWrapper.eq(Custom::getCusMembers, cusMembers);
        }
        IPage<Custom> customIPage = customMapper.selectPage(page, queryWrapper);
        long total = customIPage.getTotal();
        List<Custom> customList = customIPage.getRecords();
        List<CustomResponse> customResponses = new ArrayList<>();
        RPage<CustomResponse> rPage = new RPage<>();
        rPage.setTotal(total);
        if (!CollectionUtils.isEmpty(customList)) {
            customResponses = customList.stream()
                    .map(custom -> getCustomResponse(custom))
                    .collect(Collectors.toList());
        }
        rPage.setData(customResponses);
        return rPage;
    }

    private CustomResponse getCustomResponse(Custom custom) {
        CustomResponse customResponse = new CustomResponse();
        BeanUtils.copyProperties(custom, customResponse);
        return customResponse;
    }

    @Override
    public CustomResponse editCustom(Long id) {
        Custom custom = customMapper.selectById(id);
        CustomResponse customResponse = new CustomResponse();
        if (custom == null) throw new ServiceException("客户信息不存在,查询失败~", RStatus.FAIL);
        BeanUtils.copyProperties(custom, customResponse);
        return customResponse;
    }

    @Override
    public void delCustom(Long id) {
        if (id == null) throw new ServiceException("客户信息不存在~", RStatus.FAIL);
        customMapper.deleteById(id);
    }

    @Override
    public CustomResponse findOneCustom(String cusPhone) {
        if (StringUtils.isEmpty(cusPhone)) throw new ServiceException("手机号不能为空~", RStatus.FAIL);
        LambdaQueryWrapper<Custom> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Custom::getCusPhone, cusPhone);
        Custom custom = getOne(wrapper);
        if (custom == null) throw new ServiceException("客户不存在，无法完成下单~", RStatus.FAIL);
        CustomResponse customResponse = new CustomResponse();
        BeanUtils.copyProperties(custom, customResponse);
        return customResponse;
    }

}
