package com.dvo.NewsPortal.service;

import com.dvo.NewsPortal.entity.News;
import com.dvo.NewsPortal.web.model.request.FilterNewsRequest;
import com.dvo.NewsPortal.web.model.request.PaginationRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface NewsService {
    Page<News> findAll(PageRequest request);

    News findById(Long id);

    News save(News news, UserDetails userDetails, String categoryName);

    News update(News news, Long id);

    void deleteById(Long id);

    void deleteByIdIns(List<Long> ids);

    Page<News> filterBy(PaginationRequest paginationRequest, FilterNewsRequest filterRequest);

    List<Long> findAllIdNews();
}
