package com.dvo.NewsPortal.web.controller;

import com.dvo.NewsPortal.entity.Category;
import com.dvo.NewsPortal.mapper.CategoryMapper;
import com.dvo.NewsPortal.service.CategoryService;
import com.dvo.NewsPortal.web.model.request.PaginationRequest;
import com.dvo.NewsPortal.web.model.request.UpsertCategoryRequest;
import com.dvo.NewsPortal.web.model.response.CategoryResponse;
import com.dvo.NewsPortal.web.model.response.ErrorResponse;
import com.dvo.NewsPortal.web.model.response.ModelListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category")
public class CategoryController {
    private final CategoryMapper categoryMapper;
    private final CategoryService categoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Get all categories",
            description = "Get all categories by filter",
            tags = {"category", "id"}
    )
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER', 'ROLE_MODERATOR')")
    public ModelListResponse<CategoryResponse> findAll(@Valid PaginationRequest request) {
        Page<Category> categoryPage = categoryService.findAll(request.pageRequest());

        return ModelListResponse.<CategoryResponse>builder()
                .totalCount(categoryPage.getTotalElements())
                .data(categoryPage.stream().map(categoryMapper::categoryToResponse).toList())
                .build();
    }

    @Operation(
            summary = "Get category by ID",
            description = "Get category by ID. Return id, category name and list of news",
            tags = {"category", "id"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = CategoryResponse.class), mediaType = "application/json")}
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")}
            )
    })
    @GetMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER', 'ROLE_MODERATOR')")
    public CategoryResponse findById(@PathVariable Long categoryId) {
        return categoryMapper.categoryToResponse(categoryService.findById(categoryId));
    }


    @Operation(
            summary = "Create category",
            description = "Get category",
            tags = {"category", "id"}
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public CategoryResponse createCategory(@RequestBody UpsertCategoryRequest request) {
        Category category = categoryService.save(categoryMapper.requestToCategory(request));
        return categoryMapper.categoryToResponse(category);
    }


    @Operation(
            summary = "Update category",
            description = "Update category by ID",
            tags = {"category", "id"}
    )
    @PutMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public CategoryResponse updateCategory(@RequestBody UpsertCategoryRequest request,
                                           @PathVariable Long categoryId) {
        Category updateCategory = categoryService.update(categoryMapper.requestToCategory(request), categoryId);
        return categoryMapper.categoryToResponse(updateCategory);
    }


    @Operation(
            summary = "Delete category by ID",
            description = "Delete category by ID",
            tags = {"category", "id"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "404",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")}
            )
    })
    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public void deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteById(categoryId);
    }

}
