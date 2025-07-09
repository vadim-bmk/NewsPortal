package com.dvo.NewsPortal.web.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpsertCategoryRequest {

    @NotBlank(message = "Категория должна быть указана!")
    private String categoryName;
}
