package com.dvo.NewsPortal.web.controller;

import com.dvo.NewsPortal.configuration.SecurityConfiguration;
import com.dvo.NewsPortal.entity.Comment;
import com.dvo.NewsPortal.entity.News;
import com.dvo.NewsPortal.entity.User;
import com.dvo.NewsPortal.mapper.CommentMapper;
import com.dvo.NewsPortal.security.UserDetailsServiceImpl;
import com.dvo.NewsPortal.service.CommentService;
import com.dvo.NewsPortal.web.model.request.UpsertCommentRequest;
import com.dvo.NewsPortal.web.model.response.CommentResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentsController.class)
@Import({SecurityConfiguration.class, UserDetailsServiceImpl.class})
public class CommentsControllerTest {
    @MockBean
    private CommentMapper commentMapper;

    @MockBean
    private CommentService commentService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private UserDetails userDetails;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Comment comment;
    private CommentResponse commentResponse;
    private UpsertCommentRequest request;
    private News news;
    private User user;
    private final String URL = "/api/comment";

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).username("user").build();

        news = new News();
        news.setId(1L);
        news.setUser(user);
        news.setTitle("title");

        comment = Comment.builder().id(1L).user(user).news(news).build();
        commentResponse = CommentResponse.builder().id(1L).username("user").newsId(1L).build();

        request = UpsertCommentRequest.builder().userId(1L).newsId(1L).build();
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN", "ROLE_USER", "ROLE_MODERATOR"})
    void testGetCommentsByNewsId() throws Exception {
        when(commentService.findAllByNewsId(1L)).thenReturn(List.of(comment));
        when(commentMapper.commentToResponse(comment)).thenReturn(commentResponse);

        mockMvc.perform(get(URL)
                        .param("newsId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN", "ROLE_USER", "ROLE_MODERATOR"})
    void testGetCommentById() throws Exception {
        when(commentService.findById(1L)).thenReturn(comment);
        when(commentMapper.commentToResponse(comment)).thenReturn(commentResponse);

        mockMvc.perform(get(URL + "/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN", "ROLE_USER", "ROLE_MODERATOR"})
    void testCreateComment() throws Exception {
        when(commentMapper.requestToComment(request)).thenReturn(comment);
        when(commentService.save(comment, 1L, userDetails)).thenReturn(comment);
        when(commentMapper.commentToResponse(comment)).thenReturn(commentResponse);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN", "ROLE_USER", "ROLE_MODERATOR"})
    void testUpdateComment() throws Exception {
        when(commentMapper.requestToComment(request)).thenReturn(comment);
        when(commentService.update(comment, 1L)).thenReturn(comment);
        when(commentMapper.commentToResponse(comment)).thenReturn(commentResponse);

        mockMvc.perform(put(URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN", "ROLE_USER", "ROLE_MODERATOR"})
    void testDeleteComment() throws Exception {
        doNothing().when(commentService).deleteById(1L);

        mockMvc.perform(delete(URL + "/1"))
                .andExpect(status().isNoContent());
    }

}
