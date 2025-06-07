package com.library.exceptions;

/**
 * Исключение, возникающее при ошибках работы с базой данных.
 * Используется для оборачивания SQL-исключений и других ошибок,
 * связанных с взаимодействием с базой данных.
 */
public class DatabaseException extends RuntimeException {

    /**
     * Создает исключение с сообщением по умолчанию.
     * Сообщение: "Ошибка выполнения операции с базой данных"
     */
    public DatabaseException() {
        super("Ошибка выполнения операции с базой данных");
    }

    /**
     * Создает исключение с указанным сообщением об ошибке.
     * @param message детальное описание ошибки
     */
    public DatabaseException(String message) {
        super(message);
    }

    /**
     * Создает исключение с указанным сообщением и причиной.
     * @param message детальное описание ошибки
     * @param cause исключение, которое стало причиной данной ошибки
     */
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Создает исключение с причиной и стандартным сообщением.
     * @param cause исключение, которое стало причиной данной ошибки
     */
    public DatabaseException(Throwable cause) {
        super("Ошибка выполнения операции с базой данных", cause);
    }

    /**
     * Создает исключение с полной информацией об ошибке.
     * @param message детальное описание ошибки
     * @param cause исключение, которое стало причиной данной ошибки
     * @param enableSuppression флаг, разрешающий подавление исключения
     * @param writableStackTrace флаг, разрешающий запись stack trace
     */
    public DatabaseException(String message, Throwable cause,
                             boolean enableSuppression,
                             boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
