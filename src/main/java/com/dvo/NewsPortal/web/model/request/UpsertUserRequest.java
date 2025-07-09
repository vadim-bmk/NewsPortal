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
public class UpsertUserRequest {
    @NotBlank(message = "Имя пользователя должно быть заполнено!")
    private String username;
    @NotBlank(message = "Пароль пользователя должно быть заполнено!")
    private String password;
}
