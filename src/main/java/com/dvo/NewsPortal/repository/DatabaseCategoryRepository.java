package com.dvo.NewsPortal.repository;

import com.dvo.NewsPortal.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DatabaseCategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByCategoryName(String categoryName);

    boolean existsByCategoryName(String categoryName);
}
