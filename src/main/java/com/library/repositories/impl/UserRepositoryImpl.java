package com.library.repositories.impl;

import com.library.database.LibraryDatabase;
import com.library.database.entities.User;
import com.library.exceptions.DatabaseException;
import com.library.exceptions.UserNotFoundException;
import com.library.repositories.UserRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Реализация репозитория для работы с пользователями в базе данных.
 * Обеспечивает CRUD-операции для сущности User.
 */
public class UserRepositoryImpl implements UserRepository {
    private final Connection connection; // Соединение с базой данных

    /**
     * Основной конструктор репозитория.
     * @param connection активное соединение с базой данных
     */
    public UserRepositoryImpl(Connection connection) {
        this.connection = connection;
        createTableIfNotExists(); // Создание таблицы при инициализации
    }

    /**
     * Альтернативный конструктор для совместимости.
     * @param database экземпляр базы данных
     * @param connection активное соединение с базой данных
     */
    public UserRepositoryImpl(LibraryDatabase database, Connection connection) {
        this.connection = connection;
    }

    /**
     * Создает таблицу пользователей, если она не существует.
     * @throws DatabaseException если не удалось создать таблицу
     */
    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "email TEXT NOT NULL UNIQUE)";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new DatabaseException("Не удалось создать таблицу пользователей", e);
        }
    }

    /**
     * Получить всех пользователей из базы данных.
     * @return список всех пользователей
     * @throws DatabaseException при ошибках работы с базой данных
     */
    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        List<User> users = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                users.add(mapRowToUser(rs));
            }
            return users;
        } catch (SQLException e) {
            throw new DatabaseException("Ошибка при получении списка пользователей", e);
        }
    }

    /**
     * Найти пользователя по идентификатору.
     * @param id идентификатор пользователя
     * @return найденный пользователь
     * @throws UserNotFoundException если пользователь не найден
     * @throws DatabaseException при ошибках работы с базой данных
     */
    @Override
    public User findById(int id) throws UserNotFoundException {
        String sql = "SELECT * FROM users WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapRowToUser(rs);
            } else {
                throw new UserNotFoundException(id);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Ошибка при поиске пользователя по ID: " + id, e);
        }
    }

    /**
     * Найти пользователя по email.
     * @param email email пользователя
     * @return найденный пользователь
     * @throws UserNotFoundException если пользователь не найден
     * @throws DatabaseException при ошибках работы с базой данных
     */
    @Override
    public User findByEmail(String email) throws UserNotFoundException {
        String sql = "SELECT * FROM users WHERE email = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapRowToUser(rs);
            } else {
                throw new UserNotFoundException("Пользователь с email " + email + " не найден");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Ошибка при поиске пользователя по email: " + email, e);
        }
    }

    /**
     * Сохранить нового пользователя в базе данных.
     * @param user пользователь для сохранения
     * @throws DatabaseException при ошибках работы с базой данных
     */
    @Override
    public void save(User user) {
        String sql = "INSERT INTO users (name, email) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("Не удалось создать пользователя, ни одна строка не изменена");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                } else {
                    throw new DatabaseException("Не удалось создать пользователя, ID не получен");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Ошибка при сохранении пользователя", e);
        }
    }

    /**
     * Обновить информацию о пользователе в базе данных.
     * @param user пользователь с обновленными данными
     * @throws UserNotFoundException если пользователь не найден
     * @throws DatabaseException при ошибках работы с базой данных
     */
    @Override
    public void update(User user) throws UserNotFoundException {
        String sql = "UPDATE users SET name = ?, email = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setInt(3, user.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new UserNotFoundException(user.getId());
            }
        } catch (SQLException e) {
            throw new DatabaseException("Ошибка при обновлении пользователя с ID: " + user.getId(), e);
        }
    }

    /**
     * Удалить пользователя из базы данных.
     * @param id идентификатор пользователя
     * @throws UserNotFoundException если пользователь не найден
     * @throws DatabaseException при ошибках работы с базой данных
     */
    @Override
    public void delete(int id) throws UserNotFoundException {
        String sql = "DELETE FROM users WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new UserNotFoundException(id);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Ошибка при удалении пользователя с ID: " + id, e);
        }
    }

    /**
     * Преобразует строку результата запроса в объект User.
     * @param rs результат SQL-запроса
     * @return объект пользователя
     * @throws SQLException при ошибках чтения данных из ResultSet
     */
    private User mapRowToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        return user;
    }
}
