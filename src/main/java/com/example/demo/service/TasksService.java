package com.example.demo.service;

import com.example.demo.entities.Tasks;
import java.util.List;

/**
 * Сервис задач
 */
public interface TasksService {

    /**
     * Создает новой задачи
     * @param task - задача для создания
     */
    void create(Tasks task);

    /**
     * Возвращает список всех имеющихся задач
     * @return список задач
     */
    List<Tasks> readAll();

    /**
     * Возвращает задачу по её ID
     * @param id - ID задачи
     * @return - объект задачи с заданным ID
     */
    Tasks read(int id);

    /**
     * Обновляет задачу с заданным ID,
     * в соответствии с переданной задачей
     * @param task - задача в соответсвии с которой нужно обновить данные
     * @param id - id задачи которого нужно обновить
     * @return - true если данные были обновлены, иначе false
     */
    boolean update(Tasks task, int id);

    /**
     * Удаляет задачу с заданным ID
     * @param id - id задачи, которую нужно удалить
     * @return - true если клиент был удален, иначе false
     */
    boolean delete(int id);
}