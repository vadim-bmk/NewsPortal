package com.dvo.NewsPortal.repository.impl;

import com.dvo.NewsPortal.entity.Comment;
import com.dvo.NewsPortal.exception.EntityNotFoundException;
import com.dvo.NewsPortal.repository.CommentRepository;
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
public class InMemoryComment implements CommentRepository {
    private final Map<Long, Comment> repository = new ConcurrentHashMap<>();
    private final AtomicLong currentId = new AtomicLong(1);

    @Override
    public List<Comment> findAll() {
        return new ArrayList<>(repository.values());
    }

    @Override
    public Optional<Comment> findById(Long id) {
        return Optional.ofNullable(repository.get(id));
    }

    @Override
    public Comment save(Comment comment) {
        Long commentId = currentId.getAndIncrement();
        comment.setId(commentId);
        repository.put(commentId, comment);
        return comment;
    }

    @Override
    public Comment update(Comment comment, Long id) {
        Long commentId = comment.getId();
        Comment currentComment = repository.get(commentId);

        if (currentComment == null) {
            throw new EntityNotFoundException(MessageFormat.format("Категория по ID {0} не найдена!", commentId));
        }

        BeanUtils.copyNonNullProperties(comment, currentComment);
        currentComment.setId(commentId);
        repository.put(commentId, currentComment);
        return currentComment;
    }

    @Override
    public void deleteById(Long id) {
        Comment currentComment = repository.get(id);
        if (currentComment == null) {
            throw new EntityNotFoundException(MessageFormat.format("Категория по ID {0} не найдена!", id));
        }
        repository.remove(id);
    }

    @Override
    public void deleteByIdIns(List<Long> ids) {
        ids.forEach(repository::remove);
    }
}
