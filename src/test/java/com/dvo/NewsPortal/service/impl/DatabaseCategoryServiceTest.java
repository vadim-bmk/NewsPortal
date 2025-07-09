package com.dvo.NewsPortal.service.impl;

import com.dvo.NewsPortal.entity.Category;
import com.dvo.NewsPortal.exception.EntityExistsException;
import com.dvo.NewsPortal.exception.EntityNotFoundException;
import com.dvo.NewsPortal.repository.DatabaseCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class DatabaseCategoryServiceTest {
    @InjectMocks
    private DatabaseCategoryService categoryService;

    @Mock
    private DatabaseCategoryRepository categoryRepository;

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setCategoryName("category");
    }

    @Test
    void testFindAll() {
        Page<Category> page = new PageImpl<>(List.of(category));
        when(categoryRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Category> result = categoryService.findAll(PageRequest.of(0, 10));
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testFindById() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Category result = categoryService.findById(1L);

        assertEquals(1, result.getId());
        verify(categoryRepository).findById(1L);
    }

    @Test
    void testFindById_whenNotFound() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> categoryService.findById(1L));
    }

    @Test
    void testSave() {
        when(categoryRepository.findByCategoryName("category")).thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category result = categoryService.save(category);
        assertEquals(category, result);
        verify(categoryRepository).save(category);
    }

    @Test
    void testSave_whenCategoryExists() {
        when(categoryRepository.findByCategoryName("category")).thenReturn(Optional.of(category));

        assertThrows(EntityExistsException.class, () -> categoryService.save(category));
    }

    @Test
    void testUpdate() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category result = categoryService.update(category, 1L);
        assertEquals(category, result);
        verify(categoryRepository).save(category);
    }

    @Test
    void testDeleteById() {
        categoryService.deleteById(1L);

        verify(categoryRepository).deleteById(1L);
    }
}
