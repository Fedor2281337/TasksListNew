package com.example.demo.service;

import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с пользователями
 */
@Service
public class UserService implements UserDetailsService {
    /**
     * Репозиторий пользователей
     */
    private final UserRepository userRepository;
    /**
     * Репозиторий паролей
     */
    private final RoleRepository roleRepository;
    /**
     * Объект для работы с паролями
     */
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * Конструктор сервиса задач
     *
     * @param userRepository - репозиторий пользователей
     * @param roleRepository - репозиторий паролей
     */
    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Получить пользователя по его имени
     *
     * @param username - имя
     * @return пользователь
     * @throws UsernameNotFoundException - исключение, если пользователь не найден
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    /**
     * Получить пользователя по его имени
     *
     * @param tusername - имя
     * @return пользователь
     * @throws UsernameNotFoundException - исключение, если пользователь не найден
     */
    public UserDetails loadUserByTUsername(String tusername) throws UsernameNotFoundException {
        List<User> users = userRepository.findAllByTUsername(tusername);
        if (users == null || users.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return users.get(0);
    }

    /**
     * Получить пользователя по его id
     *
     * @param userId - id
     * @return пользователь с заданным id
     */
    public User findUserById(Long userId) {
        Optional<User> userFromDb = userRepository.findById(userId);
        return userFromDb.orElse(new User());
    }

    /**
     * Получить всех пользователей
     *
     * @return список пользователей
     */
    public List<User> allUsers() {
        return userRepository.findAll();
    }

    /**
     * Сохранить пользователя
     *
     * @param user - пользователь
     * @param role - роль
     * @return флаг, получилось ли сохранить
     */
    public boolean saveUser(User user, Role role) {
        User userFromDB = userRepository.findByUsername(user.getUsername());

        if (userFromDB != null) {
            return false;
        }

        user.setRoles(Collections.singleton(role));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    /**
     * Сохранить пользователя
     *
     * @param user      - пользователь
     * @param tusername - логин в телеграме
     * @return флаг, получилось ли сохранить
     */
    public boolean saveUser(User user, String tusername) {
        user.setTusername(tusername);
        userRepository.save(user);
        return true;
    }

    /**
     * Удалить пользователя
     *
     * @param userId - id пользователя
     * @return флаг, получилось ли удалить
     */
    public boolean deleteUser(Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

}