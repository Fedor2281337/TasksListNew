package com.example.demo.repository;

import com.example.demo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Репозиторий пользователей
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Поиск пользователя по имени
     *
     * @param username имя
     * @return пользователь
     */
    User findByUsername(String username);

    /**
     * Получить всех пользователей по заданному имени пользователя в телеграмме
     *
     * @param tusername - имя пользователя в телеграмме
     * @return список пользователей
     */
    @Query("SELECT u FROM User u WHERE u.tusername= ?1")
    List<User> findAllByTUsername(String tusername);
}