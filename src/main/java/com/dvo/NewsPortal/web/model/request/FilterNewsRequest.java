package com.dvo.NewsPortal.web.model.request;

import com.dvo.NewsPortal.validation.NewsFilterValid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@NewsFilterValid
@Builder
public class FilterNewsRequest {
    private String categoryName;
    private String userName;
}
