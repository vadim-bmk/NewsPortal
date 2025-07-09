package com.dvo.NewsPortal.repository;

import com.dvo.NewsPortal.entity.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    List<Comment> findAll();

    Optional<Comment> findById(Long id);

    Comment save(Comment comment);

    Comment update(Comment comment, Long id);

    void deleteById(Long id);

    void deleteByIdIns(List<Long> ids);
}
