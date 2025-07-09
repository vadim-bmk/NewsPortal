package com.dvo.NewsPortal.service;

import com.dvo.NewsPortal.entity.Comment;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface CommentService {
    List<Comment> findAll();

    List<Comment> findAllByNewsId(long newsId);

    Comment findById(Long id);

    Comment save(Comment comment, Long newsId, UserDetails userDetails);

    Comment update(Comment comment, Long id);

    void deleteById(Long id);

    void deleteByIdIns(List<Long> ids);

}
