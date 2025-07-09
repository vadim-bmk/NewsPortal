package com.dvo.NewsPortal.repository.impl;

import com.dvo.NewsPortal.entity.Comment;
import com.dvo.NewsPortal.entity.News;
import com.dvo.NewsPortal.entity.User;
import com.dvo.NewsPortal.exception.EntityNotFoundException;
import com.dvo.NewsPortal.repository.CommentRepository;
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
import java.util.stream.Collectors;

@Component
public class InMemoryUserRepository implements UserRepository {

    private NewsRepository newsRepository;
    private CommentRepository commentRepository;
    private final Map<Long, User> repository = new ConcurrentHashMap<>();
    private final AtomicLong currentId = new AtomicLong(1);

    @Autowired
    public void setCommentRepository(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Autowired
    public void setNewsRepository(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(repository.values());
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(repository.get(id));
    }

    @Override
    public User save(User user) {
        Long userId = currentId.getAndIncrement();
        user.setId(userId);
        repository.put(userId, user);

        return user;
    }

    @Override
    public User update(User user) {
        Long userId = user.getId();
        User currentUser = repository.get(userId);

        if (currentUser == null) {
            throw new EntityNotFoundException(MessageFormat.format("Пользователь по ID {0} не найден!", userId));
        }
        BeanUtils.copyNonNullProperties(user, currentUser);
        currentUser.setId(userId);

        repository.put(userId, currentUser);

        return currentUser;
    }

    @Override
    public void deleteById(Long id) {
        User currentUser = repository.get(id);

        if (currentUser == null) {
            throw new EntityNotFoundException(MessageFormat.format("Пользователь по ID {0} не найден!", id));
        }
        newsRepository.deleteByIdIns(currentUser.getNewsList().stream().map(News::getId).collect(Collectors.toList()));
        commentRepository.deleteByIdIns(currentUser.getCommentsList().stream().map(Comment::getId).collect(Collectors.toList()));
        repository.remove(id);
    }
}
