package com.example.demo.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Класс ролей
 */
@Entity
@Table(name = "role_table")
@Data
@NoArgsConstructor
public class Role implements GrantedAuthority {
    /**
     * id
     */
    @Id
    private Long id;
    /**
     * Имя роли
     */
    private String name;
    /**
     * Пользователи
     */
    @Transient
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    /**
     * Конструктор роли
     *
     * @param id   - id
     * @param name - имя
     */
    public Role(Long id, String name) {
        this.id = id;
        this.name = name;
        this.users = new HashSet<>();
    }

    /**
     * Строковое представление полномочий роли
     *
     * @return строковое представление
     */
    @Override
    public String getAuthority() {
        return getName();
    }
}