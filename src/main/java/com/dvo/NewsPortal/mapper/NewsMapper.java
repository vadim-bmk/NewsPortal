package com.dvo.NewsPortal.mapper;

import com.dvo.NewsPortal.entity.News;
import com.dvo.NewsPortal.mapper.delegate.NewsMapperDelegate;
import com.dvo.NewsPortal.web.model.request.NewsRequest;
import com.dvo.NewsPortal.web.model.request.UpsertNewsRequest;
import com.dvo.NewsPortal.web.model.response.NewsResponse;
import com.dvo.NewsPortal.web.model.response.ShortNewsResponse;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@DecoratedWith(NewsMapperDelegate.class)
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NewsMapper {
    NewsResponse newsToResponse(News news);

    ShortNewsResponse newsToShortResponse(News news);

    News requestToNews(UpsertNewsRequest request);

    News requestToNews(NewsRequest request);
}
