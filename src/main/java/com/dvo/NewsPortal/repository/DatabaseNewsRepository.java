package com.dvo.NewsPortal.repository;

import com.dvo.NewsPortal.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DatabaseNewsRepository extends JpaRepository<News, Long>, JpaSpecificationExecutor<News> {
    @Query("SELECT id FROM news")
    List<Long> findAllId();
}
