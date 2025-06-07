package com.library.repositories;

import com.library.database.entities.User;
import com.library.exceptions.UserNotFoundException;
import java.util.List;

/**
 * Интерфейс репозитория для работы с пользователями библиотеки.
 * Определяет контракт для операций CRUD (Create, Read, Update, Delete)
 * с сущностями пользователей в системе.
 */
public interface UserRepository {

    /**
     * Получает список всех зарегистрированных пользователей.
     * @return список объектов User (может быть пустым, если пользователей нет)
     */
    List<User> findAll();

    /**
     * Находит пользователя по уникальному идентификатору.
     * @param id числовой идентификатор пользователя
     * @return найденный объект User
     * @throws UserNotFoundException если пользователь с указанным ID не найден
     */
    User findById(int id) throws UserNotFoundException;

    /**
     * Находит пользователя по адресу электронной почты.
     * @param email email адрес пользователя
     * @return найденный объект User
     * @throws UserNotFoundException если пользователь с указанным email не найден
     */
    User findByEmail(String email) throws UserNotFoundException;

    /**
     * Сохраняет нового пользователя в системе.
     * @param user объект User для сохранения
     */
    void save(User user);

    /**
     * Обновляет данные существующего пользователя.
     * @param user объект User с обновленными данными
     * @throws UserNotFoundException если обновляемый пользователь не найден
     */
    void update(User user) throws UserNotFoundException;

    /**
     * Удаляет пользователя из системы.
     * @param id идентификатор удаляемого пользователя
     * @throws UserNotFoundException если пользователь с указанным ID не найден
     */
    void delete(int id) throws UserNotFoundException;
}
