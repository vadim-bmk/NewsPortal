package com.dvo.NewsPortal.web.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewsRequest {
    @NotBlank(message = "Заголовок новости должен быть указан!")
    private String title;

    @NotBlank(message = "Текст новости должен быть указан!")
    private String description;
}
