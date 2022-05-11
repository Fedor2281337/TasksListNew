package com.example.demo.controller;

import com.example.demo.telegram.TelegramBot;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Контроллер веб-хуков
 */
@RestController
public class WebhookController {

    /**
     * Телеграм-бот
     */
    private final TelegramBot telegramBot;

    /**
     * Конструктор контроллера веб-хуков
     * @param telegramBot - телеграм-бот
     */
    public WebhookController(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    /**
     * Обработчик запросов
     * @param update - тело запроса
     * @return апи-метод
     */
    @PostMapping("/")
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        return telegramBot.onWebhookUpdateReceived(update);
    }
}
