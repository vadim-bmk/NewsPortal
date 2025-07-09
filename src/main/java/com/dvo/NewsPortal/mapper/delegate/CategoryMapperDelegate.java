package com.dvo.NewsPortal.mapper.delegate;

import com.dvo.NewsPortal.entity.Category;
import com.dvo.NewsPortal.mapper.CategoryMapper;
import com.dvo.NewsPortal.web.model.response.CategoryResponse;

public abstract class CategoryMapperDelegate implements CategoryMapper {
    public CategoryResponse categoryToResponse(Category category) {
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(category.getId());
        categoryResponse.setCategoryName(category.getCategoryName());
        categoryResponse.setNewsCount(category.getNewsList().size());
        return categoryResponse;
    }
}
