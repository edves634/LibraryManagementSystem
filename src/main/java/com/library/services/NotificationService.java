package com.library.services;

/**
 * Сервис для отправки уведомлений пользователям
 */
public interface NotificationService {
    /**
     * Отправляет уведомление пользователю
     * @param userId ID пользователя
     * @param message Текст уведомления
     */
    void sendNotification(int userId, String message);

    /**
     * Отправляет уведомление с информацией о книге
     * @param userId ID пользователя
     * @param message Текст уведомления
     * @param bookId ID книги (опционально)
     */
    void sendNotificationWithBook(int userId, String message, int bookId);
}
