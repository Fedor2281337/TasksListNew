package com.example.demo.controller;

import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.form.RegisterForm;
import com.example.demo.service.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log
@RequestMapping("/auth")
public class AuthController {
    /**
     * Сообщение на главной странице
     */
    private static final String REGISTER_PAGE_MESSAGE = "Введите данные";
    /**
     * Заголовок главной страницы
     */
    private static final String REGISTER_PAGE_TITLE = "Регистрация";
    /**
     * Сообщение на главной странице
     */
    private static final String LOGIN_PAGE_MESSAGE = "Введите данные";
    /**
     * Заголовок главной страницы
     */
    private static final String LOGIN_PAGE_TITLE = "Вход";
    /**
     * Сервис для работы с пользователями
     */
    private final UserService userService;
    /**
     * Код регистрации администратора
     */
    @Value("${admin.registerCode}")
    private String adminRegisterCode;

    /**
     * Конструктор контроллера авторизации
     *
     * @param userService - сервис для работы с пользователями
     */
    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Страница регистрации
     *
     * @param model - модель
     * @return путь к шаблону
     */
    @GetMapping("/register")
    public String registration(Model model) {
        // задаём форму
        model.addAttribute("registerForm", new RegisterForm());
        // задаём сообщение
        model.addAttribute("message", REGISTER_PAGE_MESSAGE);
        // задаём заголовок
        model.addAttribute("title", REGISTER_PAGE_TITLE);
        return "auth/register";
    }

    /**
     * POST-запрос регистрации
     *
     * @param registerForm  - форма регистрации
     * @param bindingResult - результат связывания
     * @param model         - модель
     * @return путь к шаблону
     */
    @PostMapping("/register")
    public String addUser(@ModelAttribute("registerForm") RegisterForm registerForm, BindingResult bindingResult, Model model) {
        // если получены ошибки при связывании
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }
        // если пароли не совпадают
        if (!registerForm.getPassword().equals(registerForm.getPasswordConfirm())) {
            model.addAttribute("errorMessage", "Пароли не совпадают");
            return "auth/register";
        }
        // создаём пользователя
        User user = new User();
        user.setPassword(registerForm.getPassword());
        user.setUsername(registerForm.getUsername());

        // определяем роль пользователя
        Role role;
        // если код регистрации администратора введён верно
        if (adminRegisterCode.equals(registerForm.getCode()))
            role = new Role(1L, "ROLE_ADMIN");
        else
            role = new Role(2L, "ROLE_USER");

        // если не получилось создать пользователя в БД
        if (!userService.saveUser(user, role)) {
            model.addAttribute("errorMessage", "Пользователь с таким именем уже существует");
            return "auth/register";
        }
        log.info("register complete");
        return "redirect:/";
    }

    /**
     * Метод входа
     *
     * @param model - модель
     * @return путь к шаблону
     */
    @RequestMapping("/login")
    public String login(Model model) {
        // задаём сообщение
        model.addAttribute("message", LOGIN_PAGE_MESSAGE);
        // задаём заголовок
        model.addAttribute("title", LOGIN_PAGE_TITLE);
        return "auth/login";
    }

}
