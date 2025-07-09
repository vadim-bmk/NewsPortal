package com.dvo.NewsPortal.repository.impl;

import com.dvo.NewsPortal.entity.News;
import com.dvo.NewsPortal.exception.EntityNotFoundException;
import com.dvo.NewsPortal.repository.CategoryRepository;
import com.dvo.NewsPortal.repository.NewsRepository;
import com.dvo.NewsPortal.repository.UserRepository;
import com.dvo.NewsPortal.utils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class InMemoryNewsRepository implements NewsRepository {
    private UserRepository userRepository;
    private CategoryRepository categoryRepository;
    private final Map<Long, News> repository = new ConcurrentHashMap<>();
    private final AtomicLong currentId = new AtomicLong(1);

    @Override
    public List<News> findAll() {
        return new ArrayList<>(repository.values());
    }

    @Override
    public Optional<News> findById(Long id) {
        return Optional.ofNullable(repository.get(id));
    }

    @Override
    public News save(News news) {
        Long newsId = currentId.getAndIncrement();
        news.setId(newsId);

        repository.put(newsId, news);

        return news;
    }

    @Override
    public News update(News news) {
        Long newsId = news.getId();
        News currentNews = repository.get(newsId);

        if (currentNews == null) {
            throw new EntityNotFoundException(MessageFormat.format("Новость по ID {0} не найдена!", newsId));
        }

        BeanUtils.copyNonNullProperties(news, currentNews);
        currentNews.setId(newsId);
        repository.put(newsId, currentNews);

        return currentNews;
    }

    @Override
    public void deleteById(Long id) {
        News news = repository.get(id);
        if (news == null) {
            throw new EntityNotFoundException(MessageFormat.format("Новость по ID {0} не найдена!", id));
        }
        repository.remove(id);
    }

    @Override
    public void deleteByIdIns(List<Long> ids) {
        ids.forEach(repository::remove);
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setCategoryRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
}
