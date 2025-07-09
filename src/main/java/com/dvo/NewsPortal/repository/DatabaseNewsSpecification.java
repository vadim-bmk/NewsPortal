package com.dvo.NewsPortal.repository;

import com.dvo.NewsPortal.entity.Category;
import com.dvo.NewsPortal.entity.News;
import com.dvo.NewsPortal.entity.User;
import com.dvo.NewsPortal.web.model.request.FilterNewsRequest;
import org.springframework.data.jpa.domain.Specification;

public interface DatabaseNewsSpecification {
    static Specification<News> withFilter(FilterNewsRequest filterRequest) {
        return Specification.where(byCategoryName(filterRequest.getCategoryName()))
                .and(byUserName(filterRequest.getUserName()));
    }

    static Specification<News> byCategoryName(String categoryName) {
        return ((root, query, criteriaBuilder) -> {
            if (categoryName == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get(News.Fields.category).get(Category.Fields.categoryName), categoryName);
        });
    }

    static Specification<News> byUserName(String userName) {
        return ((root, query, criteriaBuilder) -> {
            if (userName == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get(News.Fields.user).get(User.Fields.username), userName);
        });
    }
}
