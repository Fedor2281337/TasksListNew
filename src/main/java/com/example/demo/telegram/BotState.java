package com.example.demo.telegram;

/**
 * Множество состояний бота
 */
public enum BotState {
    /**
     * Начальное состояние
     */
    STATE_START,
    /**
     * бот ждёт логина
     */
    STATE_WAIT_FOR_USERNAME,
    /**
     * бот ждёт пароля
     */
    STATE_WAIT_FOR_PASSWORD,
    /**
     * бот связан с учётной записью пользователя
     */
    STATE_CONNECTED,
    /**
     * бот ожидает нового задания
     */
    STATE_WAIT_FOR_TASK
}
