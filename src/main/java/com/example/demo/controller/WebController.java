package com.example.demo.controller;

import com.example.demo.entities.Role;
import com.example.demo.entities.Tasks;
import com.example.demo.entities.User;
import com.example.demo.form.TaskForm;
import com.example.demo.service.TasksService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashSet;
import java.util.List;

/**
 * Контроллер веб-страниц
 */
@Controller
@Log
public class WebController {
    /**
     * Сообщение на главной странице
     */
    private static final String MAIN_PAGE_MESSAGE = "Это главная страница";
    /**
     * Заголовок главной страницы
     */
    private static final String MAIN_PAGE_TITLE = "Здравствуйте!";
    /**
     * Сообщение на странице списка
     */
    private static final String LIST_PAGE_MESSAGE = "Это страница списка";
    /**
     * Заголовок на странице списка
     */
    private static final String LIST_PAGE_TITLE = "Список задач";
    /**
     * Заголовок на странице списка
     */
    private static final String EDIT_PAGE_TITLE = "Редактирование задачи";
    /**
     * Заголовок на странице списка
     */
    private static final String CONTACT_PAGE_TITLE = "Контакты";
    /**
     * Заголовок на странице списка
     */
    private static final String CONTACT_PAGE_MESSAGE = "Эта страница - заглушка";

    /**
     * Сервис задач
     */
    private final TasksService tasksService;

    /**
     * Конструктор контроллера задач
     *
     * @param tasksService сервис задач
     */
    @Autowired
    public WebController(TasksService tasksService) {
        this.tasksService = tasksService;
    }

    /**
     * Cтраница списка задач доступна по адресу `taskList`,
     *
     * @param model          - модель
     * @param authentication - данные аутентификации
     * @return - возвращает путь к шаблону
     */
    @GetMapping(value = {"/taskList"})
    public String tasksList(Model model, Authentication authentication) {
        // получаем пользователя
        User currentUser = (User) authentication.getPrincipal();

        // если он существует
        if (currentUser != null) {
            List<Tasks> tasks = null;
            // перебираем роли
            for (Role role : currentUser.getRoles())
                // если роль - администратор
                if (role.getName().equals("ROLE_ADMIN"))
                    // отправляем все задачи
                    tasks = tasksService.readAll();
            // если задачи не заполнены
            if (tasks == null)
                // заполняем задачами конкретного пользователя
                tasks = tasksService.readAll().stream().filter(
                        t -> t.getAuthor().getId().equals(currentUser.getId())
                ).toList();
            // добавляем множество его задач
            model.addAttribute("tasks", tasks);
        } else
            // иначе добавляем пустое множество
            model.addAttribute("tasks", new HashSet<>());
        // задаём сообщение
        model.addAttribute("message", LIST_PAGE_MESSAGE);
        // задаём заголовок
        model.addAttribute("title", LIST_PAGE_TITLE);
        // добавляем фому
        TaskForm taskForm = new TaskForm();
        model.addAttribute("taskForm", taskForm);
        return "pages/taskList";
    }

    /**
     * Посмотреть задачу
     *
     * @param model          - модель
     * @param authentication - данные аутентификации
     * @return - возвращает путь к шаблону
     */
    @GetMapping(value = {"/task/{id}"})
    public String showTask(Model model, @PathVariable(name = "id") int id, Authentication authentication) {
        // получаем пользователя
        User currentUser = (User) authentication.getPrincipal();
        // читаем все задачи
        Tasks task = tasksService.read(id);
        // если пользователь не является автором рассматриваемой заметки
        if (!task.getAuthor().equals(currentUser))
            // перенаправляем его к списку задач
            return "redirect:/taskList";
        // задаём сообщение
        model.addAttribute("message", "Задача: " + id);
        // задаём заголовок
        model.addAttribute("title", EDIT_PAGE_TITLE);
        // добавляем фому
        TaskForm taskForm = new TaskForm();
        taskForm.setText(task.getText());
        taskForm.setTitle(task.getTitle());
        model.addAttribute("taskForm", taskForm);
        model.addAttribute("taskId", id);
        return "pages/taskEdit";
    }

    /**
     * Изменить задачу
     *
     * @param model          - модель
     * @param taskForm       - форма с задачей
     * @param authentication - данные аутентификации
     * @return - возвращает путь к шаблону
     */
    @PostMapping(value = {"/task/{id}"})
    public String saveTask(Model model, @ModelAttribute("taskForm") TaskForm taskForm,
                           @PathVariable(name = "id") int id, Authentication authentication) {
        // получаем пользователя
        User currentUser = (User) authentication.getPrincipal();
        String text = taskForm.getText();
        String title = taskForm.getTitle();

        // если все элементы формы получены и непустые
        if (currentUser != null && text != null && text.length() > 0 && title != null && title.length() > 0) {
            // создаём новую задачу
            Tasks task = new Tasks();
            task.setAuthor(currentUser);
            task.setText(text);
            task.setTitle(title);
            // добавляем задачу в БД
            tasksService.update(task, id);
            // переходим к списку задач
            return "redirect:/taskList";
        }

        model.addAttribute("errorMessage", "Форма заполнена некорректно");
        return "redirect:/taskList";
    }

    /**
     * Добавить задачу
     *
     * @param model          - модель
     * @param taskForm       - форма с новой задачей
     * @param authentication - данные аутентификации
     * @return - возвращает путь к шаблону
     */
    @PostMapping(value = {"/addTask"})
    public String savePerson(Model model, @ModelAttribute("taskForm") TaskForm taskForm, Authentication authentication) {
        // получаем пользователя
        User currentUser = (User) authentication.getPrincipal();
        // получаем значения из формы
        String text = taskForm.getText();
        String title = taskForm.getTitle();

        // если все элементы формы получены и непустые
        if (currentUser != null && text != null && text.length() > 0 && title != null && title.length() > 0) {
            // создаём новую задачу
            Tasks task = new Tasks();
            task.setAuthor(currentUser);
            task.setText(text);
            task.setTitle(title);
            // добавляем задачу в БД
            tasksService.create(task);
            // переходим к списку задач
            return "redirect:/taskList";
        }

        model.addAttribute("errorMessage", "Форма заполнена некорректно");
        return "redirect:/taskList";
    }

    /**
     * Главная страница доступна по адресам `/` и `/index`,
     *
     * @param model - модель
     * @return - возвращает путь к шаблону
     */
    @GetMapping(value = {"/", "/index"})
    public String index(Model model) {
        // задаём сообщение
        model.addAttribute("message", MAIN_PAGE_MESSAGE);
        // задаём заголовок
        model.addAttribute("title", MAIN_PAGE_TITLE);
        // возвращаем шаблон главной страницы
        return "index";
    }

    /**
     * Главная страница доступна по адресам `/` и `/index`,
     *
     * @param model - модель
     * @return - возвращает путь к шаблону
     */
    @GetMapping(value = {"/contact"})
    public String contact(Model model) {
        // задаём сообщение
        model.addAttribute("message", CONTACT_PAGE_MESSAGE);
        // задаём заголовок
        model.addAttribute("title", CONTACT_PAGE_TITLE);
        // возвращаем шаблон главной страницы
        return "pages/contact";
    }

    /**
     * Удалить задачу
     *
     * @param id             - id задачи
     * @param authentication - данные аутентификации
     * @return - ответ на REST запрос
     */
    @PostMapping(value = "/deleteTask/{id}")
    public String delete(@PathVariable(name = "id") int id, Authentication authentication) {
        // получаем пользователя
        User currentUser = (User) authentication.getPrincipal();
        // если id автора задачи и пользователя совпадают и получилось удалить задачу
        if (tasksService.read(id).getAuthor().getId().equals(currentUser.getId()) && tasksService.delete(id))
            log.info("Задача с id=" + id + " удалена");
        // переходим к списку задач
        return "redirect:/taskList";
    }


}
