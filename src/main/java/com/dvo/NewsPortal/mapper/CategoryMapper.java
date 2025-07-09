package com.dvo.NewsPortal.mapper;

import com.dvo.NewsPortal.entity.Category;
import com.dvo.NewsPortal.mapper.delegate.CategoryMapperDelegate;
import com.dvo.NewsPortal.web.model.request.UpsertCategoryRequest;
import com.dvo.NewsPortal.web.model.response.CategoryResponse;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@DecoratedWith(CategoryMapperDelegate.class)
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {
    Category requestToCategory(UpsertCategoryRequest request);

    @Mapping(source = "categoryId", target = "id")
    Category requestToCategory(Long categoryId, UpsertCategoryRequest request);

    CategoryResponse categoryToResponse(Category category);

    List<CategoryResponse> categoryListToResponseList(List<Category> categoryList);

}
