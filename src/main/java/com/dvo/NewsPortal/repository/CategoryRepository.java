package com.dvo.NewsPortal.repository;

import com.dvo.NewsPortal.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    List<Category> findAll();

    Optional<Category> findById(Long id);

    Category save(Category category);

    Category update(Category category);

    void deleteById(Long id);
}
