package com.dvo.NewsPortal.service.impl;

import com.dvo.NewsPortal.entity.Category;
import com.dvo.NewsPortal.entity.News;
import com.dvo.NewsPortal.entity.User;
import com.dvo.NewsPortal.exception.EntityNotFoundException;
import com.dvo.NewsPortal.repository.DatabaseNewsRepository;
import com.dvo.NewsPortal.web.model.request.FilterNewsRequest;
import com.dvo.NewsPortal.web.model.request.PaginationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class DatabaseNewsServiceTest {
    @InjectMocks
    private DatabaseNewsService newsService;

    @Mock
    private DatabaseNewsRepository newsRepository;

    @Mock
    private DatabaseUserService userService;

    @Mock
    private DatabaseCategoryService categoryService;

    @Mock
    private UserDetails userDetails;

    private News news;
    private User user;
    private Category category;

    @BeforeEach
    void setUp() {
        news = new News();
        news.setId(1L);
        news.setUser(new User());
        news.setCategory(new Category());
        news.setTitle("title");
        news.setDescription("description");

        user = User.builder().id(1L).username("user").password("12345").build();

        category = new Category();
        category.setId(1L);
        category.setCategoryName("category");
    }

    @Test
    void testFindAll() {
        Page<News> page = new PageImpl<>(List.of(news));
        when(newsRepository.findAll(PageRequest.of(0, 10))).thenReturn(page);

        Page<News> result = newsService.findAll(PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
    }

    @Test
    void testFindById() {
        when(newsRepository.findById(1L)).thenReturn(Optional.of(news));

        News result = newsService.findById(1L);

        assertEquals(news, result);
        verify(newsRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_whenNotFound() {
        when(newsRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> newsService.findById(1L));
    }

    @Test
    void testSave() {
        when(userDetails.getUsername()).thenReturn("user");
        when(userService.findByUsername("user")).thenReturn(user);
        when(categoryService.findByName("category")).thenReturn(category);
        when(newsRepository.save(news)).thenReturn(news);

        News result = newsService.save(news, userDetails, "category");

        assertEquals(news, result);
        verify(userService, times(1)).findByUsername("user");
        verify(categoryService, times(1)).findByName("category");
        verify(newsRepository, times(1)).save(news);
    }

    @Test
    void testUpdate() {
        when(newsRepository.findById(1L)).thenReturn(Optional.of(news));
        when(newsRepository.save(news)).thenReturn(news);

        News result = newsService.update(news, 1L);

        assertEquals(news, result);
    }

    @Test
    void testUpdate_whenNotFound() {
        when(newsRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> newsService.update(news, 1L));
    }

    @Test
    void testDeleteById() {
        newsService.deleteById(1L);

        verify(newsRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteByIdIns() {
        List<Long> ids = List.of(1L, 2L, 3L);

        newsService.deleteByIdIns(ids);

        verify(newsRepository, times(3)).deleteById(anyLong());
    }

    @Test
    void testFilterBy() {
        PaginationRequest request = PaginationRequest.builder().pageNumber(0).pageSize(10).build();
        FilterNewsRequest filter = FilterNewsRequest.builder().categoryName("category").build();

        when(newsRepository.findAll(
                any((Class<Specification<News>>) (Class<?>) Specification.class),
                eq(PageRequest.of(request.getPageNumber(), request.getPageSize())))
        ).thenReturn(new PageImpl<>(List.of(news)));

        Page<News> result = newsService.filterBy(request, filter);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testFindAAllIdNews() {
        when(newsRepository.findAllId()).thenReturn(List.of(1L, 2L, 3L));

        List<Long> result = newsService.findAllIdNews();

        assertEquals(List.of(1L, 2L, 3L), result);
    }
}
