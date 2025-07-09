package com.dvo.NewsPortal.repository.impl;

import com.dvo.NewsPortal.entity.Category;
import com.dvo.NewsPortal.exception.EntityNotFoundException;
import com.dvo.NewsPortal.repository.CategoryRepository;
import com.dvo.NewsPortal.utils.BeanUtils;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class InMemoryCategoryRepository implements CategoryRepository {
    private final Map<Long, Category> repository = new ConcurrentHashMap<>();
    private final AtomicLong currentId = new AtomicLong(1);

    @Override
    public List<Category> findAll() {
        return new ArrayList<>(repository.values());
    }

    @Override
    public Optional<Category> findById(Long id) {
        return Optional.ofNullable(repository.get(id));
    }

    @Override
    public Category save(Category category) {
        Long categoryId = currentId.getAndIncrement();
        category.setId(categoryId);

        repository.put(categoryId, category);
        return category;
    }

    @Override
    public Category update(Category category) {
        Long categoryId = category.getId();
        Category currentCategory = repository.get(categoryId);
        if (currentCategory == null) {
            throw new EntityNotFoundException(MessageFormat.format("Категория по ID {0} не найдена!", categoryId));
        }

        BeanUtils.copyNonNullProperties(category, currentCategory);
        currentCategory.setId(categoryId);
        repository.put(categoryId, currentCategory);
        return currentCategory;
    }

    @Override
    public void deleteById(Long id) {
        Category category = repository.get(id);
        if (category == null) {
            throw new EntityNotFoundException(MessageFormat.format("Категория по ID {0} не найдена!", id));
        }
        repository.remove(id);
    }
}
