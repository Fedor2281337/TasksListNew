package com.example.demo.config;

import com.example.demo.telegram.TelegramBot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;

/**
 * Настройки приложения
 */
@Configuration
public class AppConfig {
    /**
     * Настройки бота
     */
    private final TelegramBotConfig botConfig;

    /**
     * Конструктор настроек приложения
     *
     * @param botConfig - настройки бота
     */
    public AppConfig(TelegramBotConfig botConfig) {
        this.botConfig = botConfig;
    }

    /**
     * Бин обработки связывания веб-хука
     *
     * @return бин обработки связывания веб-хука
     */
    @Bean
    public SetWebhook setWebhookInstance() {
        return SetWebhook.builder().url(botConfig.getWebHookPath()).build();
    }

    /**
     * Бин телеграм-бота
     *
     * @param setWebhook        - бин связывания веб-хука
     * @return бин телеграм-бота
     */
    @Bean
    public TelegramBot springWebhookBot(SetWebhook setWebhook) {
        // создаём бота
        TelegramBot bot = new TelegramBot( setWebhook);
        // заполняем значениями его поля
        bot.setBotToken(botConfig.getBotToken());
        bot.setBotUsername(botConfig.getUserName());
        bot.setBotPath(botConfig.getWebHookPath());
        // возвращаем бота
        return bot;
    }
}
