package com.dvo.NewsPortal.service.impl;

import com.dvo.NewsPortal.entity.Category;
import com.dvo.NewsPortal.entity.Comment;
import com.dvo.NewsPortal.entity.News;
import com.dvo.NewsPortal.entity.User;
import com.dvo.NewsPortal.exception.EntityNotFoundException;
import com.dvo.NewsPortal.repository.DatabaseCommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class DatabaseCommentServiceTest {
    @InjectMocks
    private DatabaseCommentService commentService;

    @Mock
    private DatabaseCommentRepository commentRepository;

    @Mock
    private DatabaseUserService userService;

    @Mock
    private DatabaseNewsService newsService;

    private Comment comment;

    @BeforeEach
    void setUp(){
        User user = User.builder()
                .id(1L)
                .username("user")
                .build();

        News news = new News();
        news.setId(1L);
        news.setUser(user);
        news.setCategory(new Category());
        news.setTitle("title");
        news.setDescription("description");

        comment = Comment.builder()
                .id(1L)
                .text("test")
                .user(user)
                .news(news)
                .build();
    }

    @Test
    void testFindAll () {
        when(commentRepository.findAll()).thenReturn(List.of(comment));

        List<Comment> result = commentService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testFindAllByNewsId () {
        when(commentRepository.findAllByNewsId(1L)).thenReturn(List.of(comment));

        List<Comment> result = commentService.findAllByNewsId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testFindById () {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        Comment result = commentService.findById(1L);

        assertEquals(comment, result);
        verify(commentRepository).findById(1L);
    }

    @Test
    void testFindById_whenNotFound () {
        when(commentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> commentService.findById(1L));
    }


}
