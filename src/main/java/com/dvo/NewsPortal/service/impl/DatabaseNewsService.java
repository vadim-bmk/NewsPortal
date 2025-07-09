package com.dvo.NewsPortal.service.impl;

import com.dvo.NewsPortal.entity.Category;
import com.dvo.NewsPortal.entity.News;
import com.dvo.NewsPortal.entity.User;
import com.dvo.NewsPortal.exception.EntityNotFoundException;
import com.dvo.NewsPortal.repository.DatabaseNewsRepository;
import com.dvo.NewsPortal.repository.DatabaseNewsSpecification;
import com.dvo.NewsPortal.service.NewsService;
import com.dvo.NewsPortal.utils.BeanUtils;
import com.dvo.NewsPortal.web.model.request.FilterNewsRequest;
import com.dvo.NewsPortal.web.model.request.PaginationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DatabaseNewsService implements NewsService {

    private final DatabaseNewsRepository newsRepository;
    private final DatabaseUserService userService;
    private final DatabaseCategoryService categoryService;

    @Override
    public Page<News> findAll(PageRequest request) {
        log.info("Call findAll in DatabaseNewsService");
        return newsRepository.findAll(request);
    }

    @Override
    public News findById(Long id) {
        log.info("Call findById in DatabaseNewsService by ID: {}", id);
        return newsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Новость с ID {0} не найдена!", id)));
    }

    @Override
    @Transactional
    public News save(News news, UserDetails userDetails, String categoryName) {
        log.info("Call save in DatabaseNewsService by news: {}", news);
        User user = userService.findByUsername(userDetails.getUsername());
        Category category = categoryService.findByName(categoryName);
        news.setCategory(category);
        news.setUser(user);
        user.addNews(news);
        category.addNews(news);
        return newsRepository.save(news);
    }

    @Override
    @Transactional
    public News update(News news, Long newsId) {
        log.info("Call update in DatabaseNewsService by ID: {}", newsId);
        News existedNews = findById(newsId);
        BeanUtils.copyNonNullProperties(news, existedNews);

        return newsRepository.save(existedNews);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        log.info("Call deleteByID in DatabaseNewsService by ID: {}", id);
        newsRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteByIdIns(List<Long> ids) {
        log.info("Call deleteByIdIns in DatabaseNewsService by list ID: {}", ids);
        ids.forEach(newsRepository::deleteById);
    }

    @Override
    public Page<News> filterBy(PaginationRequest paginationRequest, FilterNewsRequest filterRequest) {
        log.info("Call filterBy in DatabaseNewsService by filter");
        return newsRepository.findAll(DatabaseNewsSpecification.withFilter(filterRequest), paginationRequest.pageRequest());
    }

    @Override
    public List<Long> findAllIdNews() {
        log.info("Call findAllIdNews in DatabaseNewsService");
        return newsRepository.findAllId();
    }
}
