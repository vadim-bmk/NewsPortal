package com.dvo.NewsPortal.service.impl;

import com.dvo.NewsPortal.entity.Comment;
import com.dvo.NewsPortal.exception.EntityNotFoundException;
import com.dvo.NewsPortal.repository.DatabaseCommentRepository;
import com.dvo.NewsPortal.service.CommentService;
import com.dvo.NewsPortal.utils.BeanUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DatabaseCommentService implements CommentService {
    private final DatabaseCommentRepository commentRepository;
    private final DatabaseUserService userService;
    private final DatabaseNewsService newsService;


    @Override
    public List<Comment> findAll() {
        log.info("Call findAll in DatabaseCommentService");
        return commentRepository.findAll();
    }

    @Override
    public List<Comment> findAllByNewsId(long newsId) {
        log.info("Call findAllByNewsId in DatabaseCommentService by news ID: {}", newsId);
        return commentRepository.findAllByNewsId(newsId);
    }

    @Override
    public Comment findById(Long id) {
        log.info("Call findById in DatabaseCommentService by ID: {}", id);
        return commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Комментарий с ID {0} не найден!", id)));
    }

    @Override
    @Transactional
    public Comment save(Comment comment, Long newsId, UserDetails userDetails) {
        log.info("Call save in DatabaseCommentService by comment: {}", comment);
        comment.setUser(userService.findByUsername(userDetails.getUsername()));
        comment.setNews(newsService.findById(newsId));
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public Comment update(Comment comment, Long id) {
        log.info("Call update in DatabaseCommentService by ID: {}", id);
        Comment existedComment = findById(id);
        BeanUtils.copyNonNullProperties(comment, existedComment);
        return commentRepository.save(existedComment);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        log.info("Call deleteById in DatabaseCommentService by ID: {}", id);
        commentRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteByIdIns(List<Long> ids) {
        log.info("Call deleteByIdIns in DatabaseCommentService by list ID: {}", ids);
        ids.forEach(commentRepository::deleteById);
    }
}
