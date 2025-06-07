package com.library.services;

import com.library.database.entities.User;
import java.util.List;

/**
 * Сервис для работы с пользователями библиотеки.
 * Обеспечивает базовые CRUD-операции с пользователями.
 */
public interface UserService {
    // Получить список всех пользователей
    List<User> getAllUsers();

    // Найти пользователя по ID
    User getUserById(int id);

    // Добавить нового пользователя
    void addUser(User user);

    // Обновить данные пользователя
    void updateUser(User user);

    // Удалить пользователя по ID
    void deleteUser(int id);
}
