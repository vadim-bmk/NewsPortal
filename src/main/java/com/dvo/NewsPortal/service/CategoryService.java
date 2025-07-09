package com.dvo.NewsPortal.service;

import com.dvo.NewsPortal.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface CategoryService {
    Page<Category> findAll(PageRequest request);

    Category findById(Long id);

    Category save(Category category);

    Category update(Category category, Long id);

    Category findByName(String name);

    void deleteById(Long id);
    //Category saveWithNews(Category category, List<News> news);
}
