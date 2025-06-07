package com.library.services.impl;

import com.library.database.entities.User;
import com.library.repositories.UserRepository;
import com.library.services.UserService;
import java.util.List;

/**
 * Реализация сервиса для работы с пользователями.
 * Обеспечивает бизнес-логику управления пользователями библиотеки.
 */
public class UserServiceImpl implements UserService {
    // Репозиторий для работы с данными пользователей
    private final UserRepository userRepository;

    /**
     * Конструктор сервиса.
     * @param userRepository репозиторий пользователей (не может быть null)
     */
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Получить список всех зарегистрированных пользователей.
     * @return список пользователей (может быть пустым)
     */
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Найти пользователя по идентификатору.
     * @param id уникальный идентификатор пользователя
     * @return найденный пользователь
     * @throws com.library.exceptions.UserNotFoundException если пользователь не найден
     */
    @Override
    public User getUserById(int id) {
        return userRepository.findById(id);
    }

    /**
     * Добавить нового пользователя в систему.
     * @param user объект пользователя для сохранения
     * @throws com.library.exceptions.DatabaseException при ошибках сохранения
     */
    @Override
    public void addUser(User user) {
        userRepository.save(user);
    }

    /**
     * Обновить данные пользователя.
     * @param user объект пользователя с обновленными данными
     * @throws com.library.exceptions.UserNotFoundException если пользователь не найден
     * @throws com.library.exceptions.DatabaseException при ошибках обновления
     */
    @Override
    public void updateUser(User user) {
        userRepository.update(user);
    }

    /**
     * Удалить пользователя из системы.
     * @param id идентификатор удаляемого пользователя
     * @throws com.library.exceptions.UserNotFoundException если пользователь не найден
     * @throws com.library.exceptions.DatabaseException при ошибках удаления
     */
    @Override
    public void deleteUser(int id) {
        userRepository.delete(id);
    }
}
