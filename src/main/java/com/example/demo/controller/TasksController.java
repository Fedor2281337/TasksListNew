package com.example.demo.controller;

import com.example.demo.entities.Tasks;
import com.example.demo.entities.User;
import com.example.demo.service.TasksService;
import com.example.demo.service.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * Контроллер задач
 */
@RestController
@RequestMapping("/tasks")
@Log
public class TasksController {
    /**
     * Сервис задач
     */
    private final TasksService tasksService;
    /**
     * Сервис задач
     */
    private final UserService userService;

    /**
     * Конструктор контроллера задач
     *
     * @param tasksService сервис задач
     */
    @Autowired
    public TasksController(TasksService tasksService, UserService userService) {
        this.tasksService = tasksService;
        this.userService = userService;
    }


    /**
     * Создать задачу
     *
     * @param task - задача
     * @return - ответ на REST запрос
     */
    @PostMapping(value = "/create")
    public ResponseEntity<?> create(@RequestBody Tasks task) {
        tasksService.create(task);
        log.info("Задача " + task.getTitle() + " создана");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Авторизация для REST-запросов
     *
     * @return пользователя
     */
    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    User getAuthUser() {
        // получаем объект авторизации
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // если его нет, возвращаем null
        if (auth == null) {
            return null;
        }
        // получаем ссылку на пользователя из запроса
        Object principal = auth.getPrincipal();
        // если объект является экземпляром класса User, сохраняем его
        User user = (principal instanceof User) ? (User) principal : null;
        // возвращаем пользователя
        return Objects.nonNull(user) ? (User) this.userService.loadUserByUsername(user.getUsername()) : null;
    }

    /**
     * Получить список всех задач
     *
     * @return - ответ на REST запрос
     */
    @GetMapping(value = "/list")
    public ResponseEntity<List<Tasks>> readAll() {
        // получаем все задачи
        final List<Tasks> tasks = tasksService.readAll();
        // выводим в лог, сколько задач найдено
        log.info("Найдено " + tasks.size() + " задач");
        // если есть хотя бы одная задача
        return !tasks.isEmpty()
                ? new ResponseEntity<>(tasks, HttpStatus.OK)  // возвращаем в ответе список задач, статус ответа `OK`
                : new ResponseEntity<>(HttpStatus.NOT_FOUND); // иначе статус ответа  `NOT_FOUND`
    }

    /**
     * Получить задачу по id
     *
     * @param id - id задачи
     * @return - ответ на REST запрос
     */
    @GetMapping(value = "/list/{id}")
    public ResponseEntity<Tasks> read(@PathVariable(name = "id") int id) {
        // ищем задачу по id
        final Tasks task = tasksService.read(id);
        // выводим сообщение о том, какая задача запрошена
        log.info("Запрошена задача " + task);
        // если задача найдена
        return task != null
                ? new ResponseEntity<>(task, HttpStatus.OK)   // возвращаем в ответе задачу, статус ответа `OK`
                : new ResponseEntity<>(HttpStatus.NOT_FOUND); // иначе статус ответа  `NOT_FOUND`
    }

    /**
     * Обновить заданную задачу
     *
     * @param task новая задача
     * @param id   - id старой задачи
     * @return - ответ на REST запрос
     */
    @PostMapping(value = "/update/{id}")
    public ResponseEntity<Tasks> update(@RequestBody Tasks task, @PathVariable(name = "id") int id) {
        // выводим инофрмацию, какая задача будет модифицирована
        log.info("Обновляется задача с id=" + id + " новые данные: " + task);
        // если получилось обновить задачу
        return tasksService.update(task, id)
                ? new ResponseEntity<>(task, HttpStatus.OK)   // возвращаем в ответе задачу, статус ответа `OK`
                : new ResponseEntity<>(HttpStatus.NOT_FOUND); // иначе статус ответа  `NOT_FOUND`
    }

    /**
     * Удалить задачу
     *
     * @param id - id задачи
     * @return - ответ на REST запрос
     */
    @PostMapping(value = "/delete/{id}")
    public ResponseEntity<Tasks> delete(@PathVariable(name = "id") int id) {
        // выводим сообщение с информацией о том, какую задачу нужно удалить
        log.info("Задача с id=" + id + " удалена");
        // если получилось удалить задачу
        return tasksService.delete(id)
                ? new ResponseEntity<>(HttpStatus.OK)         // статус ответа `OK`
                : new ResponseEntity<>(HttpStatus.NOT_FOUND); // иначе статус ответа  `NOT_FOUND`
    }

}
