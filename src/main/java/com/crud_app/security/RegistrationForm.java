package com.crud_app.security;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationForm {

    @NotBlank(message = "Имя пользователя обязательно")
    @Size(min = 3, max = 50, message = "Имя пользователя: от 3 до 50 символов")
    private String username;

    @NotBlank(message = "Пароль обязателен")
    @Size(min = 4, max = 100, message = "Пароль: минимум 4 символа")
    private String password;

    @NotBlank(message = "Подтверждение пароля обязательно")
    private String confirmPassword;
}
