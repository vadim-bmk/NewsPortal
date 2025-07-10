package com.dvo.NewsPortal.web.controller;

import com.dvo.NewsPortal.configuration.SecurityConfiguration;
import com.dvo.NewsPortal.entity.Category;
import com.dvo.NewsPortal.entity.News;
import com.dvo.NewsPortal.entity.User;
import com.dvo.NewsPortal.mapper.NewsMapper;
import com.dvo.NewsPortal.security.UserDetailsServiceImpl;
import com.dvo.NewsPortal.service.CategoryService;
import com.dvo.NewsPortal.service.NewsService;
import com.dvo.NewsPortal.web.model.request.FilterNewsRequest;
import com.dvo.NewsPortal.web.model.request.PaginationRequest;
import com.dvo.NewsPortal.web.model.request.UpsertNewsRequest;
import com.dvo.NewsPortal.web.model.response.NewsResponse;
import com.dvo.NewsPortal.web.model.response.ShortNewsResponse;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NewsController.class)
@Import({SecurityConfiguration.class, UserDetailsServiceImpl.class})
public class NewsControllerTest {
    @MockBean
    private NewsService newsService;

    @MockBean
    private NewsMapper newsMapper;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private UserDetails userDetails;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final String URL = "/api/news";
    private News news;
    private UpsertNewsRequest request;
    private NewsResponse newsResponse;
    private ShortNewsResponse shortNewsResponse;
    private Category category;

    @BeforeEach
    void setUp() {
        User user = User.builder().id(1L).username("user").build();

        category = new Category();
        category.setId(1L);
        category.setCategoryName("category");

        news = new News();
        news.setId(1L);
        news.setTitle("title");
        news.setUser(user);
        news.setCategory(category);

        request = UpsertNewsRequest.builder().title("title").categoryId(1L).build();
        newsResponse = new NewsResponse();
        newsResponse.setId(1L);
        newsResponse.setTitle("title");
        newsResponse.setUserName("user");
        newsResponse.setCategoryName("category");

        shortNewsResponse = ShortNewsResponse.builder().id(1L).title("title").categoryName("category").userName("user").build();
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN", "ROLE_USER", "ROLE_MODERATOR"})
    void testFindAll() throws Exception {
        PaginationRequest paginationRequest = PaginationRequest.builder().pageNumber(0).pageSize(10).build();
        FilterNewsRequest filter = FilterNewsRequest.builder().userName("user").build();
        Page<News> page = new PageImpl<>(List.of(news));

        when(newsService.filterBy(paginationRequest, filter)).thenReturn(page);
        when(newsMapper.newsToShortResponse(news)).thenReturn(shortNewsResponse);

        mockMvc.perform(get(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("pageNumber", paginationRequest.getPageNumber().toString())
                        .param("pageSize", paginationRequest.getPageSize().toString())
                        .param("userName", filter.getUserName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCount").value(1))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].title").value("title"))
                .andExpect(jsonPath("$.data[0].userName").value("user"))
                .andExpect(jsonPath("$.data[0].categoryName").value("category"));
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN", "ROLE_USER", "ROLE_MODERATOR"})
    void testFindById() throws Exception {
        when(newsService.findById(1L)).thenReturn(news);
        when(newsMapper.newsToResponse(news)).thenReturn(newsResponse);

        mockMvc.perform(get(URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("title"));
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN", "ROLE_USER", "ROLE_MODERATOR"})
    void testCreateNews() throws Exception {
        when(categoryService.findById(request.getCategoryId())).thenReturn(category);
        when(newsMapper.requestToNews(request)).thenReturn(news);
        when(newsService.save(news, userDetails, category.getCategoryName())).thenReturn(news);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN", "ROLE_USER", "ROLE_MODERATOR"})
    void testUpdateNews() throws Exception {
        when(newsMapper.requestToNews(request)).thenReturn(news);
        when(newsService.update(news, 1L)).thenReturn(news);
        when(newsMapper.newsToResponse(news)).thenReturn(newsResponse);

        mockMvc.perform(put(URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN", "ROLE_USER", "ROLE_MODERATOR"})
    void testDeleteNews() throws Exception {
        doNothing().when(newsService).deleteById(1L);

        mockMvc.perform(delete(URL + "/1"))
                .andExpect(status().isNoContent());
    }
}
