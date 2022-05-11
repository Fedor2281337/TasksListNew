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
public class TaskForm {
    /**
     * Заголовок
     */
    private String title;
    /**
     * Текст
     */
    private String text;
}
