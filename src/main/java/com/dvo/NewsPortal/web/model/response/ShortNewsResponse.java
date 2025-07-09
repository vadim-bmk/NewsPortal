package com.dvo.NewsPortal.web.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShortNewsResponse {
    private Long id;
    private String title;
    private String description;
    private String userName;
    private String categoryName;
    private int commentsCount;
}
