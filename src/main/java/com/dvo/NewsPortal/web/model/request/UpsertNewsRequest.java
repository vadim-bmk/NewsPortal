package com.dvo.NewsPortal.web.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpsertNewsRequest {
    @NotBlank(message = "Заголовок новости должен быть указан!")
    private String title;

    @NotBlank(message = "Текст новости должен быть указан!")
    private String description;

    @NotNull(message = "ID категории должен быть указан!")
    @Positive(message = "ID категории должно быть больше 0!")
    private Long categoryId;
}
