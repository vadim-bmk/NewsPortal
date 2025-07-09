package com.dvo.NewsPortal.web.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsResponse {
    private Long id;
    private String title;
    private String description;
    private String userName;
    private String categoryName;
    List<CommentResponse> commentList = new ArrayList<>();
}
