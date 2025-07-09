package com.dvo.NewsPortal.repository;

import com.dvo.NewsPortal.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DatabaseCommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM comments c WHERE c.news.id = :newsId")
    List<Comment> findAllByNewsId(Long newsId);

}
