package com.ly.storeserver.admin.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ly.storeserver.admin.models.entity.Category;
import com.ly.storeserver.admin.mapper.CategoryMapper;
import com.ly.storeserver.admin.models.request.CategoryRequest;
import com.ly.storeserver.admin.models.response.CategoryResponse;
import com.ly.storeserver.admin.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ly
 * @since 2020-04-08 22:00
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<CategoryResponse> getParentCategory() {
        LambdaQueryWrapper<Category> wrapper = Wrappers.<Category>lambdaQuery().eq(Category::getParentId, 1);
        List<Category> categories = categoryMapper.selectList(wrapper);
        if (!CollectionUtils.isEmpty(categories)) {
            List<CategoryResponse> categoryResponses = categories.stream().
                    map(category -> createCategoryRes(category)).collect(Collectors.toList());
            return categoryResponses;
        }
        return null;
    }

    @Override
    public JSONArray getCategory(Integer parentId) {
        JSONArray objects = new JSONArray();
        LambdaQueryWrapper<Category> wrapper = Wrappers.<Category>lambdaQuery().eq(Category::getParentId, parentId);
        List<Category> categories = categoryMapper.selectList(wrapper);
        for (Category category : categories) {
            JSONObject object = new JSONObject();
            object.put("id", category.getId());
            object.put("categoryName", category.getCategoryName());
            JSONArray childrenCategory = getCategory(category.getId());
            if (childrenCategory.size() > 0) {
                object.put("children", childrenCategory);
            }
            objects.add(object);
        }
        return objects;
    }

    @Override
    public CategoryResponse updateCategory(CategoryRequest categoryRequest) {
        Integer id = categoryRequest.getId();
        String categoryName = categoryRequest.getCategoryName();
        Category category = categoryMapper.selectById(id);
        if (category != null && !StringUtils.isBlank(categoryName)) {
            category.setCategoryName(categoryName);
            categoryMapper.updateById(category);
        }
        return createCategoryRes(category);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delCategory(Integer[] ids) {
        Wrapper<Category> wrapper = Wrappers.<Category>lambdaQuery().in(Category::getParentId, ids);
        List<Category> categories = categoryMapper.selectList(wrapper);
        if (!CollectionUtils.isEmpty(categories)) {
            List<Integer> idList = categories.stream().map(category -> category.getId()).collect(Collectors.toList());
            categoryMapper.deleteBatchIds(idList);
        }
        categoryMapper.deleteBatchIds(CollectionUtils.arrayToList(ids));
    }

    @Override
    public void addCategory(CategoryRequest categoryRequest, String userMobile) {
        Category category = new Category();
        category.setCategoryName(categoryRequest.getCategoryName());
        category.setParentId(categoryRequest.getId());
        category.setDeleted(0);
        category.setVersion(0);
        category.setOperation(userMobile);
        save(category);
    }

    private CategoryResponse createCategoryRes(Category category) {
        if (category == null) return null;
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(category.getId());
        categoryResponse.setCategoryName(category.getCategoryName());
        categoryResponse.setParentId(category.getParentId());
        categoryResponse.setOperation(category.getOperation());
        categoryResponse.setCreateTime(category.getCreateTime());
        categoryResponse.setUpdateTime(category.getUpdateTime());
        return categoryResponse;
    }
}
