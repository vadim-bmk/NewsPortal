package com.dvo.NewsPortal.aop;

import com.dvo.NewsPortal.entity.Comment;
import com.dvo.NewsPortal.entity.News;
import com.dvo.NewsPortal.exception.EntityNotFoundException;
import com.dvo.NewsPortal.exception.OwnerCheckException;
import com.dvo.NewsPortal.repository.DatabaseCommentRepository;
import com.dvo.NewsPortal.repository.DatabaseNewsRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

@Aspect
@RequiredArgsConstructor
@Component
@Slf4j
public class OwnerAspect {
    private final DatabaseNewsRepository newsRepository;
    private final DatabaseCommentRepository commentRepository;
    private static final String HTTP_CLIENT_HEADER = "X-Client-Api-Key";

    @Before("@annotation(NewsOwnerCheck)")
    public void checkNewsOwner() {
        HttpServletRequest request = getCurrentRequest();
        Long newsId = Long.valueOf(getPathVariables(request).get("newsId"));

        Long currentUserId = getCurrentUserId();
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new EntityNotFoundException("News not found by ID"));

        if (!news.getUser().getId().equals(currentUserId)) {
            log.info("You are not author this news!");
            throw new OwnerCheckException("You are not author this news!");
        }

    }

    @Before("@annotation(CommentOwnerCheck)")
    public void checkCommentOwner() {
        HttpServletRequest request = getCurrentRequest();
        Long commentId = Long.valueOf(getPathVariables(request).get("commentId"));

        Long currentUserId = getCurrentUserId();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found by ID"));

        if (!comment.getUser().getId().equals(currentUserId)) {
            log.info("You are not author this comment!");
            throw new OwnerCheckException("You are not author this comment!");
        }
    }

    private HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes.getRequest();
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> getPathVariables(HttpServletRequest request) {

        return (Map<String, String>)
                request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
    }

    private Long getCurrentUserId() {
        HttpServletRequest request = getCurrentRequest();
        String userIdHeader = request.getHeader(HTTP_CLIENT_HEADER);

        if (userIdHeader == null) {
            log.info("Header X-Client-Api-Key not found!");
            throw new OwnerCheckException("Header X-Client-Api-Key not found!");
        }

        try {
            return Long.valueOf(userIdHeader);
        } catch (NumberFormatException e) {
            log.info("Incorrect value of header X-Client-Api-Key!");
            throw new OwnerCheckException("Incorrect value of header X-Client-Api-Key!");

        }
    }

}
