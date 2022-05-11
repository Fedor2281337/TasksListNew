package com.example.demo.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Настройки телеграм-бота
 */
@Component
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TelegramBotConfig {
    /**
     * Путь до сервера обработки веб=хуков
     */
    @Value("${telegrambot.webhookPath}")
    String webHookPath;
    /**
     * Имя пользователя для бота
     */
    @Value("${telegrambot.userName}")
    String userName;
    /**
     * Токен бота
     */
    @Value("${telegrambot.botToken}")
    String botToken;
}
