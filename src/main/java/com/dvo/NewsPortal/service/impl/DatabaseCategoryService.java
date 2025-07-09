package com.dvo.NewsPortal.service.impl;

import com.dvo.NewsPortal.entity.Category;
import com.dvo.NewsPortal.exception.EntityExistsException;
import com.dvo.NewsPortal.exception.EntityNotFoundException;
import com.dvo.NewsPortal.repository.DatabaseCategoryRepository;
import com.dvo.NewsPortal.service.CategoryService;
import com.dvo.NewsPortal.utils.BeanUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;

@Service
@RequiredArgsConstructor
@Slf4j
public class DatabaseCategoryService implements CategoryService {
    private final DatabaseCategoryRepository categoryRepository;

    @Override
    public Page<Category> findAll(PageRequest request) {
        log.info("Call findAll in DatabaseCategoryService");
        return categoryRepository.findAll(request);
    }

    @Override
    public Category findById(Long id) {
        log.info("Call findById in DatabaseCategoryService by ID: {}", id);
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Категория с ID {0} не найдена!", id)));
    }

    @Override
    @Transactional
    public Category save(Category category) {
        log.info("Call save in DatabaseCategoryService by Category: {}", category);
        if (findByName(category.getCategoryName()) != null) {
            log.info("Category is exists");
            throw new EntityExistsException(MessageFormat.format("Категория с названием {0} уже существует!", category.getCategoryName()));
        }
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public Category update(Category category, Long id) {
        log.info("Call update in DatabaseCategoryService by ID: {}", id);
        Category existedCategory = findById(id);
        BeanUtils.copyNonNullProperties(category, existedCategory);
        return categoryRepository.save(existedCategory);
    }

    @Override
    public Category findByName(String categoryName) {
        log.info("Call findByName in DatabaseCategoryService by category name: {}", categoryName);
        return categoryRepository.findByCategoryName(categoryName).orElse(null);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        log.info("Call deleteById in DatabaseCategoryService by ID: {}", id);
        categoryRepository.deleteById(id);
    }
}
