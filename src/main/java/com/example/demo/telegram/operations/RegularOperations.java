package com.example.demo.telegram.operations;

import com.example.demo.entities.Tasks;
import com.example.demo.entities.User;
import com.example.demo.service.TasksService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.telegram.TelegramBot.*;

/**
 * Сервис обычных операций
 */
@Service
@Log
public class RegularOperations {
    /**
     * Сервис задач
     */
    TasksService tasksService;

    /**
     * Конструктор сервиса обычных операций
     *
     * @param tasksService - сервис задач
     */
    @Autowired
    RegularOperations(TasksService tasksService) {
        this.tasksService = tasksService;
    }

    /**
     * Операция старта
     *
     * @param message - пришедшее сообщение
     * @return ответ
     */
    public String hello(Message message, SendMessage sendMessage) {
        showInlineMenu(sendMessage);
        return "Привет, " + message.getFrom().getFirstName() + "!\n" +
                "Вы написали:\n" + message.getText();
    }

    /**
     * Добавить задание
     *
     * @param message       сообщение с текстом
     * @param connectedUser подключенный пользователь
     * @return ответ
     */
    public String addTask(Message message, User connectedUser) {
        Tasks task = new Tasks();
        task.setTitle("Задача от бота");
        task.setAuthor(connectedUser);
        task.setText(message.getText());
        tasksService.create(task);
        return "Задача добавлена";
    }

    /**
     * Отобразить меню
     *
     * @param sendMessage ответное сообщение
     */
    public void showReplyMenu(SendMessage sendMessage) {
        // создаём разметку клавиатуры ответа
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        // разрешаем клавиатуру
        replyKeyboardMarkup.setSelective(true);
        // разрешаем её масштабирование
        replyKeyboardMarkup.setResizeKeyboard(true);
        // создаём список строк клавиатуры
        List<KeyboardRow> keyboard = new ArrayList<>();
        // создаём строки, в каждой по одной кнопке
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        row1.add(new KeyboardButton(COMMAND_ADD_TASK));
        row2.add(new KeyboardButton(COMMAND_LIST_TASK));
        // добавляем строки в список строк
        keyboard.add(row1);
        keyboard.add(row2);
        // сохраняем клавиатуру в разметку
        replyKeyboardMarkup.setKeyboard(keyboard);
        // сохраняем разметку
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
    }

    /**
     * Отобразить меню
     *
     * @param sendMessage ответное сообщение
     */
    public void showInlineMenu(SendMessage sendMessage) {
        // создаём разметку встроенной клавиатуры
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        // создаём кнопку добавления задачи
        InlineKeyboardButton addTaskBtn = new InlineKeyboardButton();
        addTaskBtn.setText(BTN_ADD_TASK_TEXT);
        addTaskBtn.setCallbackData(BTN_ADD_TASK_CALLBACK_NAME);

        // создаём кнопку списка задач
        InlineKeyboardButton taskListBtn = new InlineKeyboardButton();
        taskListBtn.setText(BTN_LIST_TASK_TEXT);
        taskListBtn.setCallbackData(BTN_LIST_TASK_CALLBACK_NAME);
        // создаём строки таблицы
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow1.add(addTaskBtn);
        keyboardButtonsRow2.add(taskListBtn);
        // добавляем строки в список строк
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        // сохраняем клавиатуру в разметку
        inlineKeyboardMarkup.setKeyboard(rowList);
        // сохраняем разметку
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
    }


    /**
     * Отобразить список заданий
     *
     * @param connectedUser подключенный пользователь
     * @return ответ
     */
    public String listTask(User connectedUser) {
        String result = "Задачи:\n\n";
        // получаем список задачам конкретного пользователя
        List<Tasks> tasksList = tasksService.readAll().stream().filter(
                t -> t.getAuthor().getId().equals(connectedUser.getId())
        ).toList();
        // формируем строку вывода
        for (Tasks task : tasksList) {
            result += ">" + task.getTitle() + "\n" +
                    task.getText() + "\n\n";
        }
        result += "\n";
        return result;
    }

}
