package com.dvo.NewsPortal.web.controller;

import com.dvo.NewsPortal.aop.AuthorCheck;
import com.dvo.NewsPortal.entity.News;
import com.dvo.NewsPortal.mapper.NewsMapper;
import com.dvo.NewsPortal.service.CategoryService;
import com.dvo.NewsPortal.service.NewsService;
import com.dvo.NewsPortal.web.model.request.FilterNewsRequest;
import com.dvo.NewsPortal.web.model.request.NewsRequest;
import com.dvo.NewsPortal.web.model.request.PaginationRequest;
import com.dvo.NewsPortal.web.model.request.UpsertNewsRequest;
import com.dvo.NewsPortal.web.model.response.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/news")
public class NewsController {
    private final NewsService newsService;
    private final NewsMapper newsMapper;
    private final CategoryService categoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Get news",
            description = "Get all news by filter. Return id, title, description, user name, category name and count of comments",
            tags = {"news", "id"}
    )
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER', 'ROLE_MODERATOR')")
    public ModelListResponse<ShortNewsResponse> filterBy(@Valid PaginationRequest paginationRequest,
                                                         FilterNewsRequest filterNewsRequest) {
        Page<News> newsPage = newsService.filterBy(paginationRequest, filterNewsRequest);
        return ModelListResponse.<ShortNewsResponse>builder()
                .totalCount(newsPage.getTotalElements())
                .data(newsPage.stream().map(newsMapper::newsToShortResponse).toList())
                .build();
    }

    @GetMapping("/{newsId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Get news by ID",
            description = "Get news by ID. Return id, title, description, user name, category name and list of comments",
            tags = {"news", "id"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = NewsResponse.class), mediaType = "application/json")}
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")}
            )
    })
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER', 'ROLE_MODERATOR')")
    public NewsResponse findById(@PathVariable Long newsId) {
        return newsMapper.newsToResponse(newsService.findById(newsId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create news",
            description = "Create news",
            tags = {"news", "id"}
    )
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER', 'ROLE_MODERATOR')")
    public NewsResponse createNews(@AuthenticationPrincipal UserDetails userDetails,
                                   @RequestBody UpsertNewsRequest request) {
        News news = newsService.save(newsMapper.requestToNews(request), userDetails, categoryService.findById(request.getCategoryId()).getCategoryName());
        return newsMapper.newsToResponse(news);

    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Update news",
            description = "Update news by ID",
            tags = {"news", "id"}
    )
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER', 'ROLE_MODERATOR')")
    @AuthorCheck
    public NewsResponse updateNews(@AuthenticationPrincipal UserDetails userDetails,
                                   @RequestBody NewsRequest request,
                                   @PathVariable Long id) {
        News updateNews = newsService.update(newsMapper.requestToNews(request), id);
        return newsMapper.newsToResponse(updateNews);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete news",
            description = "Delete news by ID",
            tags = {"news", "id"}
    )
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER', 'ROLE_MODERATOR')")
    @AuthorCheck
    public void deleteNews(@AuthenticationPrincipal UserDetails userDetails,
                           @PathVariable Long id) {
        newsService.deleteById(id);
    }


}
