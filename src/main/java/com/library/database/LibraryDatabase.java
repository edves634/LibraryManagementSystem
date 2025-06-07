package com.library.database;

import com.library.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Класс для работы с подключением к базе данных библиотеки.
 * Реализует AutoCloseable для использования в try-with-resources.
 */
public class LibraryDatabase implements AutoCloseable {
    // Конфигурация базы данных (URL, драйвер и т.д.)
    private final DatabaseConfig config;

    // JDBC-соединение с базой данных
    private Connection connection;

    /**
     * Конструктор инициализирует подключение к БД
     * @param config конфигурация базы данных (не может быть null)
     * @throws SQLException если не удалось установить соединение
     */
    public LibraryDatabase(DatabaseConfig config) throws SQLException {
        // Проверяем что конфиг не null
        this.config = Objects.requireNonNull(config, "DatabaseConfig не может быть null");
        // Инициализируем БД
        initializeDatabase();
    }

    /**
     * Инициализирует подключение к базе данных
     * @throws SQLException если возникли проблемы с подключением
     */
    private void initializeDatabase() throws SQLException {
        try {

            Class.forName(config.getDriver());
            this.connection = DriverManager.getConnection(config.getUrl());

        } catch (ClassNotFoundException e) {
            throw new SQLException("Драйвер базы данных не найден: " + config.getDriver(), e);
        }
    }

    /**
     * Возвращает соединение с БД. Если соединение закрыто - создает новое.
     * @return активное соединение с БД
     * @throws SQLException если не удалось установить соединение
     */
    public Connection getConnection() throws SQLException {
        // Если соединение null или закрыто - переподключаемся
        if (connection == null || connection.isClosed()) {
            initializeDatabase();
        }
        return connection;
    }

    /**
     * Закрывает соединение с БД. Реализация AutoCloseable.
     * Гарантирует освобождение ресурсов даже при возникновении ошибки.
     */
    @Override
    public void close() {
        try {
            // Если соединение существует и не закрыто - закрываем его
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            // Логируем ошибку закрытия соединения
            System.err.println("Ошибка при закрытии соединения с БД: " + e.getMessage());
        } finally {
            // Гарантируем что connection будет null после закрытия
            connection = null;
        }
    }

    /**
     * Проверяет активность соединения с БД
     * @return true если соединение активно, false в противном случае
     */
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            // В случае ошибки считаем что соединение не активно
            return false;
        }
    }
}
