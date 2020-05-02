package com.ly.storeserver.admin.controller;


import com.alibaba.fastjson.JSONArray;
import com.ly.storeserver.admin.models.request.CategoryRequest;
import com.ly.storeserver.admin.models.response.CategoryResponse;
import com.ly.storeserver.admin.service.CategoryService;
import com.ly.storeserver.common.bean.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * @author ly
 * @since 2020-04-08 22:00
 */
@RestController
@RequestMapping("/category/")
@Api(value = "服装分类API", tags = "服装分类API")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @ApiOperation(value = "查询一级分类", notes = "查询一级分类接口")
    @GetMapping("getParentCategory")
    public R getParentCategory() {
        List<CategoryResponse> parentCategoryListRes = categoryService.getParentCategory();
        return new R<>(parentCategoryListRes);
    }

    @ApiOperation(value = "查询所有分类", notes = "查询所有分类接口")
    @GetMapping("getCategory")
    public R getCategory() {
        Integer parentId = 1;
        JSONArray categoryListRes = categoryService.getCategory(parentId);
        return new R<>(categoryListRes);
    }

    @ApiOperation(value = "查询子级分类", notes = "查询子级分类接口")
    @GetMapping("getChildren")
    public R getChildren(Integer id) {
        JSONArray categoryListRes =categoryService.getCategory(id);
        return new R<>(categoryListRes);
    }

    @ApiOperation(value = "添加分类", notes = "添加分类接口")
    @PostMapping("addCategory")
    public R addCategory(@RequestBody  @Validated CategoryRequest categoryRequest,
                               @ApiIgnore @RequestAttribute(value = "userMobile") String userMobile) {
        categoryService.addCategory(categoryRequest, userMobile);
        return new R<>("添加分类成功~");
    }

    @ApiOperation(value = "修改分类", notes = "修改分类接口")
    @PostMapping("updateCategory")
    public R updateParentCategory(@RequestBody CategoryRequest categoryRequest) {
        CategoryResponse parentCategoryRes = categoryService.updateCategory(categoryRequest);
        return new R<>("修改分类成功~",parentCategoryRes);
    }

    @ApiOperation(value = "删除分类", notes = "删除分类接口")
    @GetMapping("delCategory")
    public R delParentCategory(@RequestParam(value = "ids") Integer[] ids) {
        categoryService.delCategory(ids);
        return new R<>("删除分类成功~");
    }

}

