package com.dvo.NewsPortal.repository;

import com.dvo.NewsPortal.entity.News;

import java.util.List;
import java.util.Optional;

public interface NewsRepository {
    List<News> findAll();

    Optional<News> findById(Long id);

    News save(News news);

    News update(News news);

    void deleteById(Long id);

    void deleteByIdIns(List<Long> ids);

}
