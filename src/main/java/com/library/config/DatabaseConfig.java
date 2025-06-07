package com.library.config;

/**
 * Класс конфигурации для работы с базой данных
 */
public class DatabaseConfig {
    // URL для подключения к SQLite базе данных (файл library.db в той же директории)
    private static final String DB_URL = "jdbc:sqlite:library.db";

    // Класс драйвера JDBC для SQLite
    private static final String DRIVER = "org.sqlite.JDBC";

    /**
     * Возвращает URL для подключения к базе данных
     * @return строка с URL подключения
     */
    public String getUrl() {
        return DB_URL;
    }

    /**
     * Возвращает имя класса JDBC драйвера
     * @return строка с именем класса драйвера
     */
    public String getDriver() {
        return DRIVER;
    }

    /**
     * Метод-заглушка для совместимости (SQLite не требует логина)
     * @return пустую строку
     */
    public String getUsername() {
        return "";
    }

    /**
     * Метод-заглушка для совместимости (SQLite не требует пароля)
     * @return пустую строку
     */
    public String getPassword() {
        return "";
    }
}
