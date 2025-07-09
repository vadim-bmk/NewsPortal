package com.dvo.NewsPortal.mapper.delegate;

import com.dvo.NewsPortal.entity.News;
import com.dvo.NewsPortal.mapper.CommentMapper;
import com.dvo.NewsPortal.mapper.NewsMapper;
import com.dvo.NewsPortal.web.model.response.NewsResponse;
import com.dvo.NewsPortal.web.model.response.ShortNewsResponse;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class NewsMapperDelegate implements NewsMapper {
    @Autowired
    private NewsMapper newsMapper;
    @Autowired
    private CommentMapper commentMapper;

    public NewsResponse newsToResponse(News news) {
        NewsResponse newsResponse = new NewsResponse();
        newsResponse.setId(news.getId());
        newsResponse.setTitle(news.getTitle());
        newsResponse.setDescription(news.getDescription());

        newsResponse.setUserName(news.getUser().getUsername());
        newsResponse.setCategoryName(news.getCategory().getCategoryName());
        newsResponse.setCommentList(news.getCommentsList().stream()
                .map(commentMapper::commentToResponse)
                .toList());

        return newsResponse;
    }

    public ShortNewsResponse newsToShortResponse(News news) {
        ShortNewsResponse shortNewsResponse = new ShortNewsResponse();
        shortNewsResponse.setId(news.getId());
        shortNewsResponse.setTitle(news.getTitle());
        shortNewsResponse.setDescription(news.getDescription());
        shortNewsResponse.setUserName(news.getUser().getUsername());
        shortNewsResponse.setCategoryName(news.getCategory().getCategoryName());
        shortNewsResponse.setCommentsCount(news.getCommentsList().size());

        return shortNewsResponse;
    }
}
