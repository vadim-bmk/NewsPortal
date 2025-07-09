package com.dvo.NewsPortal.web.controller;

import com.dvo.NewsPortal.aop.AuthorCheck;
import com.dvo.NewsPortal.entity.Comment;
import com.dvo.NewsPortal.mapper.CommentMapper;
import com.dvo.NewsPortal.service.CommentService;
import com.dvo.NewsPortal.web.model.request.CommentRequest;
import com.dvo.NewsPortal.web.model.request.UpsertCommentRequest;
import com.dvo.NewsPortal.web.model.response.CommentResponse;
import com.dvo.NewsPortal.web.model.response.ErrorResponse;
import com.dvo.NewsPortal.web.model.response.ModelListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentsController {
    private final CommentMapper commentMapper;
    private final CommentService commentService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Get comments",
            description = "Get all comments by newsId",
            tags = {"comments", "id"}
    )
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER', 'ROLE_MODERATOR')")
    public ModelListResponse<CommentResponse> getCommentsByNewsId(@RequestParam Long newsId) {
        List<Comment> commentList = commentService.findAllByNewsId(newsId);

        return ModelListResponse.<CommentResponse>builder()
                .totalCount((long) commentList.size())
                .data(commentList.stream().map(commentMapper::commentToResponse).toList())
                .build();
    }

    @GetMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Get comment",
            description = "Get comment by ID. Return ID, news ID, user name and text of comment",
            tags = {"comments", "id"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = CommentResponse.class), mediaType = "application/json")}
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")}
            )
    })
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER', 'ROLE_MODERATOR')")
    public CommentResponse getCommentById(@PathVariable Long commentId) {
        return commentMapper.commentToResponse(commentService.findById(commentId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create comment",
            description = "Create comment",
            tags = {"comment", "id"}
    )
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER', 'ROLE_MODERATOR')")
    public CommentResponse createComment(@AuthenticationPrincipal UserDetails userDetails,
                                         @RequestBody UpsertCommentRequest request) {
        Comment newComment = commentService.save(commentMapper.requestToComment(request), request.getNewsId(), userDetails);
        return commentMapper.commentToResponse(newComment);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Update comment",
            description = "Update comment by ID",
            tags = {"comment", "id"}
    )
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER', 'ROLE_MODERATOR')")
    @AuthorCheck
    public CommentResponse updateComment(@AuthenticationPrincipal UserDetails userDetails,
                                         @RequestBody CommentRequest request,
                                         @PathVariable Long id) {
        Comment updateComment = commentService.update(commentMapper.requestToComment(request), id);
        return commentMapper.commentToResponse(updateComment);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete comment",
            description = "Delete comment by ID",
            tags = {"comment", "id"}
    )
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER', 'ROLE_MODERATOR')")
    @AuthorCheck
    public void deleteComment(@AuthenticationPrincipal UserDetails userDetails,
                              @PathVariable Long id) {
        commentService.deleteById(id);
    }

}
