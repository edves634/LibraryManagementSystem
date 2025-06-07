package com.library.repositories;

import com.library.database.entities.Book;
import com.library.exceptions.BookNotFoundException;

import java.util.List;

/**
 * Интерфейс репозитория для работы с книгами в библиотеке.
 * Определяет основные CRUD-операции для сущности Book.
 */
public interface BookRepository {

    /**
     * Получить список всех книг из хранилища.
     * @return список объектов Book (может быть пустым, если книг нет)
     */
    List<Book> findAll();

    /**
     * Найти книгу по уникальному идентификатору.
     * @param id идентификатор книги
     * @return найденный объект Book
     * @throws BookNotFoundException если книга с указанным ID не найдена
     */
    Book findById(int id) throws BookNotFoundException;

    /**
     * Сохранить новую книгу в хранилище.
     * @param book объект Book для сохранения
     */
    void save(Book book);

    /**
     * Обновить информацию о существующей книге.
     * @param book объект Book с обновленными данными
     * @throws BookNotFoundException если книга для обновления не найдена
     */
    void update(Book book) throws BookNotFoundException;

    /**
     * Удалить книгу из хранилища.
     * @param id идентификатор удаляемой книги
     * @throws BookNotFoundException если книга с указанным ID не найдена
     */
    void delete(int id) throws BookNotFoundException;
}
