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
public class UpsertCommentRequest {
    @NotBlank(message = "Текст комментария должно быть указано!")
    private String text;

    @NotNull(message = "ID пользователя должно быть указано!")
    @Positive(message = "ID пользователя должно быть больше 0!")
    private Long userId;

    @NotNull(message = "ID новости должно быть указано!")
    @Positive(message = "ID новости должно быть больше 0!")
    private Long newsId;
}
