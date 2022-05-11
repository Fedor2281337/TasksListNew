package com.example.demo.repository;

import com.example.demo.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий ролей
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
}