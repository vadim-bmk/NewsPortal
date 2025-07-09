package com.dvo.NewsPortal.mapper;

import com.dvo.NewsPortal.entity.Comment;
import com.dvo.NewsPortal.web.model.request.CommentRequest;
import com.dvo.NewsPortal.web.model.request.UpsertCommentRequest;
import com.dvo.NewsPortal.web.model.response.CommentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {
    Comment requestToComment(UpsertCommentRequest request);

    Comment requestToComment(CommentRequest request);

    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "news.id", target = "newsId")
    CommentResponse commentToResponse(Comment comment);
}
