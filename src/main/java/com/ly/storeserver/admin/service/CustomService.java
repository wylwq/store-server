package com.ly.storeserver.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ly.storeserver.admin.models.entity.Custom;
import com.ly.storeserver.admin.models.request.CustomQueryRequest;
import com.ly.storeserver.admin.models.request.CustomRequest;
import com.ly.storeserver.admin.models.response.CustomResponse;
import com.ly.storeserver.common.bean.RPage;

/**
 * @author ly
 * @since 2020-05-06 22:30
 */
public interface CustomService extends IService<Custom> {

    /**
     * 保存客户
     * @param customRequest
     */
    void saveCustom(CustomRequest customRequest);

    /**
     * 查询客户列表
     * @param customQueryRequest
     * @return
     */
    RPage<CustomResponse> customList(CustomQueryRequest customQueryRequest);

    /**
     * 编辑客户
     * @param id
     */
    CustomResponse editCustom(Long id);

    /**
     * 删除客户
     * @param id
     */
    void delCustom(Long id);

    /**
     * 根据手机号查询客户
     * @param cusPhone
     * @return
     */
    CustomResponse findOneCustom(String cusPhone);
}
