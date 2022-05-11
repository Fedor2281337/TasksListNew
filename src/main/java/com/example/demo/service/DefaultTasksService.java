package com.example.demo.service;

import com.example.demo.entities.Tasks;
import com.example.demo.repository.TasksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Сервис задач по умолчанию
 */
@Service
public class DefaultTasksService implements TasksService {
    /**
     * Репозиторий базы данных задач
     */
    private final TasksRepository tasksRepository;

    /**
     * Создает новую задачу
     *
     * @param task - задача для создания
     */
    @Override
    public void create(Tasks task) {
        tasksRepository.save(task);
    }

    /**
     * Конструктор сервиса задач
     *
     * @param tasksRepository репозиторий збд задач
     */
    @Autowired
    public DefaultTasksService(TasksRepository tasksRepository) {
        this.tasksRepository = tasksRepository;
    }

    /**
     * Возвращает список всех имеющихся задач
     *
     * @return список задач
     */
    @Override
    public List<Tasks> readAll() {
        return tasksRepository.findAll();
    }

    /**
     * Возвращает задачу по её ID
     *
     * @param id - ID задачи
     * @return - объект задачи с заданным ID
     */
    @Override
    public Tasks read(int id) {
        return tasksRepository.getById(id);
    }

    /**
     * Обновляет задачу с заданным ID,
     * в соответствии с переданной задачей
     *
     * @param task - задача в соответсвии с которой нужно обновить данные
     * @param id   - id задачи которого нужно обновить
     * @return - true если данные были обновлены, иначе false
     */
    @Override
    public boolean update(Tasks task, int id) {
        // если существует задача с таким id
        if (tasksRepository.existsById(id)) {
            // меняем id у задачи
            task.setId(id);
            // при сохранении в базу данных будет найдена запись с таким же
            // id, как у сохраняемого, после чего все поля записи будут приведены в
            // соответствие с полями переданного объекта
            tasksRepository.save(task);
            return true;
        }

        return false;
    }

    /**
     * Удаляет задачу с заданным ID
     *
     * @param id - id задачи, которую нужно удалить
     * @return - true если клиент был удален, иначе false
     */
    @Override
    public boolean delete(int id) {
        // если существует задача с таким id
        if (tasksRepository.existsById(id)) {
            // удаляем её
            tasksRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
