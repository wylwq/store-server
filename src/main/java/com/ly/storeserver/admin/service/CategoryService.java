package com.ly.storeserver.admin.service;

import com.alibaba.fastjson.JSONArray;
import com.ly.storeserver.admin.models.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ly.storeserver.admin.models.request.CategoryRequest;
import com.ly.storeserver.admin.models.response.CategoryResponse;

import java.util.List;

/**
 * @author ly
 * @since 2020-04-08 22:00
 */
public interface CategoryService extends IService<Category> {

    /**
     * 查询一级分类接口
     * @return
     */
    List<CategoryResponse> getParentCategory();

    /**
     * 查询所有分类接口
     * @return
     */
    JSONArray getCategory(Integer parentId);

    /**
     * 修改分类接口
     * @param categoryRequest
     * @return
     */
    CategoryResponse updateCategory(CategoryRequest categoryRequest);

    /**
     * 修改分类接口
     * @param ids
     */
    void delCategory(Integer[] ids);

    /**
     * 添加分类接口
     * @param categoryRequest
     */
    void addCategory(CategoryRequest categoryRequest, String userMobile);
}
