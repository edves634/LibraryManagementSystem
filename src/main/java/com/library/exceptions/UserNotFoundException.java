package com.library.exceptions;

/**
 * Исключение, возникающее при попытке доступа к несуществующему пользователю.
 * Содержит информацию об идентификаторе пользователя, который не был найден.
 */
public class UserNotFoundException extends RuntimeException {
    private final int userId;  // ID пользователя, который не был найден

    /**
     * Создает исключение с указанным ID пользователя.
     * Сообщение генерируется автоматически в формате:
     * "Пользователь с ID {userId} не найден"
     * @param userId идентификатор не найденного пользователя
     */
    public UserNotFoundException(int userId) {
        super("Пользователь с ID " + userId + " не найден");
        this.userId = userId;
    }

    /**
     * Создает исключение с кастомным сообщением об ошибке.
     * ID пользователя устанавливается в -1 (неопределенное значение).
     * @param message детальное сообщение об ошибке
     */
    public UserNotFoundException(String message) {
        super(message);
        this.userId = -1;  // -1 означает, что ID не определен
    }

    /**
     * Создает исключение с указанным ID пользователя и причиной ошибки.
     * Сообщение генерируется автоматически.
     * @param userId идентификатор не найденного пользователя
     * @param cause исключение, которое стало причиной ошибки
     */
    public UserNotFoundException(int userId, Throwable cause) {
        super("Пользователь с ID " + userId + " не найден", cause);
        this.userId = userId;
    }

    /**
     * Возвращает ID пользователя, который не был найден.
     * @return числовой ID пользователя или -1, если ID не был указан
     */
    public int getUserId() {
        return userId;
    }
}
