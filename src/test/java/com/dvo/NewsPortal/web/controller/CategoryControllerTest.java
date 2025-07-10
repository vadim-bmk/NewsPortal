package com.dvo.NewsPortal.web.controller;

import com.dvo.NewsPortal.configuration.SecurityConfiguration;
import com.dvo.NewsPortal.entity.Category;
import com.dvo.NewsPortal.mapper.CategoryMapper;
import com.dvo.NewsPortal.security.UserDetailsServiceImpl;
import com.dvo.NewsPortal.service.CategoryService;
import com.dvo.NewsPortal.web.model.request.PaginationRequest;
import com.dvo.NewsPortal.web.model.request.UpsertCategoryRequest;
import com.dvo.NewsPortal.web.model.response.CategoryResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
@Import({SecurityConfiguration.class, UserDetailsServiceImpl.class})
public class CategoryControllerTest {
    @MockBean
    private CategoryMapper categoryMapper;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Category category;
    private CategoryResponse categoryResponse;
    private UpsertCategoryRequest request;
    private final String URL = "/api/category";

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setCategoryName("category");

        categoryResponse = CategoryResponse.builder().id(1L).categoryName("category").newsCount(5).build();
        request = UpsertCategoryRequest.builder().categoryName("category").build();
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void testFindAll() throws Exception {
        Page<Category> page = new PageImpl<>(List.of(category));
        PaginationRequest request = PaginationRequest.builder().pageNumber(0).pageSize(10).build();

        when(categoryService.findAll(request.pageRequest())).thenReturn(page);
        when(categoryMapper.categoryToResponse(category)).thenReturn(categoryResponse);

        mockMvc.perform(get(URL)
                        .param("pageNumber", request.getPageNumber().toString())
                        .param("pageSize", request.getPageSize().toString()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void testFindById() throws Exception {
        when(categoryService.findById(1L)).thenReturn(category);
        when(categoryMapper.categoryToResponse(category)).thenReturn(categoryResponse);

        mockMvc.perform(get(URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.categoryName").value("category"));
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN", "ROLE_MODERATOR"})
    void testCreateCategory_withAdminOrModeratorAuthority() throws Exception {
        when(categoryMapper.requestToCategory(request)).thenReturn(category);
        when(categoryService.save(category)).thenReturn(category);
        when(categoryMapper.categoryToResponse(category)).thenReturn(categoryResponse);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void testCreateCategory_withUserAuthority() throws Exception {
        when(categoryMapper.requestToCategory(request)).thenReturn(category);
        when(categoryService.save(category)).thenReturn(category);
        when(categoryMapper.categoryToResponse(category)).thenReturn(categoryResponse);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN", "ROLE_MODERATOR"})
    void testUpdateCategory_withAdminOrModeratorAuthority() throws Exception {
        when(categoryMapper.requestToCategory(request)).thenReturn(category);
        when(categoryService.update(category, 1L)).thenReturn(category);
        when(categoryMapper.categoryToResponse(category)).thenReturn(categoryResponse);

        mockMvc.perform(put(URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void testUpdateCategory_withUserAuthority() throws Exception {
        when(categoryMapper.requestToCategory(request)).thenReturn(category);
        when(categoryService.update(category, 1L)).thenReturn(category);
        when(categoryMapper.categoryToResponse(category)).thenReturn(categoryResponse);

        mockMvc.perform(put(URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN", "ROLE_MODERATOR"})
    void testDeleteCategory_withAdminOrModeratorAuthority() throws Exception {
        doNothing().when(categoryService).deleteById(anyLong());

        mockMvc.perform(delete(URL + "/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void testDeleteCategory_withUserAuthority() throws Exception {
        doNothing().when(categoryService).deleteById(anyLong());

        mockMvc.perform(delete(URL + "/1"))
                .andExpect(status().isForbidden());
    }
}
