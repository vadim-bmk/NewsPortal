package com.dvo.NewsPortal.aop;

import com.dvo.NewsPortal.entity.RoleType;
import com.dvo.NewsPortal.service.CommentService;
import com.dvo.NewsPortal.service.NewsService;
import com.dvo.NewsPortal.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

@Aspect
@Component
@RequiredArgsConstructor
public class AuthorCheckAspect {
    private final UserService userService;
    private final NewsService newsService;
    private final CommentService commentService;

    @Around("@annotation(AuthorCheck) && args(userDetails, ..)")
    public Object authorCheck(ProceedingJoinPoint pjp, UserDetails userDetails) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        var pathVar = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        Long pathVarId = Long.valueOf(pathVar.get("id"));

        Long currentUserId = userService.findByUsername(userDetails.getUsername()).getId();

        Long authorId = switch (pjp.getSignature().getDeclaringTypeName()) {

            case "com.example.springbootnewsportal.web.controller.NewsController",
                 "com.example.springbootnewsportal.service.impl.DatabaseNewsService" ->
                    newsService.findById(pathVarId).getUser().getId();

            case "com.example.springbootnewsportal.web.controller.CommentsController",
                 "com.example.springbootnewsportal.service.impl.DatabaseCommentService" ->
                    commentService.findById(pathVarId).getUser().getId();

            default -> null;
        };

        var roles = userService.findByUsername(userDetails.getUsername()).getRoles();
        boolean isAdminOrModerator = roles.stream()
                .anyMatch(r -> r.getAuthority() == RoleType.ROLE_ADMIN || r.getAuthority() == RoleType.ROLE_MODERATOR);


        if (!currentUserId.equals(authorId) && !isAdminOrModerator) {
            throw new AccessDeniedException("Not access!!!");
        }

        try {
            return pjp.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

    }
}
