package com.example.demo.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Форма добавления задачи
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterForm {
    /**
     * Имя пользователя
     */
    private String username;
    /**
     * Пароль
     */
    private String password;
    /**
     * Подтверждение пароля
     */
    private String passwordConfirm;
    /**
     * Код регистрации
     */
    private String code;
}
