package com.example.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Класс задачи
 */
@Entity
@Table(name = "tasks_table")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tasks {
    /**
     * Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable=false, updatable=false)
    private Integer id;
    /**
     * Заголовок
     */
    private String title;
    /**
     * Автор
     */
    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User author;
    /**
     * Текст
     */
    private String text;
}
