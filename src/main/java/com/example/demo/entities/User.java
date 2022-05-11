package com.example.demo.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

/**
 * Класс пользователя
 */
@Entity
@Table(name = "user_table")
@Data
@NoArgsConstructor
public class User implements UserDetails {
    /**
     * Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Роли пользователя
     */
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;
    /**
     * Задачи пользователя
     */
    @Transient
    @OneToMany(mappedBy="author", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Tasks> tasks;
    /**
     * Имя пользователя
     */
    private String username;
    /**
     * Пароль
     */
    private String password;
    /**
     * Имя телеграм пользователя
     */
    private String tusername;

    /**
     * Получить роли пользователя
     *
     * @return список ролей
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    /**
     * Проверка, не истёк ли срок действия пользователя
     *
     * @return флаг, не истёк ли срок действия пользователя
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Проверка, не заблокирован ли пользователь
     *
     * @return флаг, не заблокирован ли пользователь
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Проверка, не истёк ли срок действия учётных данных
     *
     * @return флаг, не истёк ли срок действия учётных данных
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Проверка, не заблокирован ли пользователь
     *
     * @return флаг, не заблокирован ли пользователь
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

}
