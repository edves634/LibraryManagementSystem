package com.library.services;

import com.library.database.entities.Book;
import java.util.List;

/**
 * Сервис для управления книгами в библиотеке.
 * Предоставляет методы для работы с книгами: поиск, добавление,
 * обновление, удаление, аренда и возврат книг.
 */
public interface BookService {
    // Получить все книги
    List<Book> getAllBooks();

    // Найти книгу по ID
    Book getBookById(int id);

    // Добавить новую книгу
    void addBook(Book book);

    // Обновить данные книги
    void updateBook(Book book);

    // Удалить книгу по ID
    void deleteBook(int id);

    // Взять книгу в аренду
    void borrowBook(int bookId, int userId);

    // Вернуть книгу в библиотеку
    void returnBook(int bookId);

    // Поиск книг по названию
    List<Book> searchByTitle(String title);

    // Поиск книг по автору
    List<Book> searchByAuthor(String author);

    // Поиск книг по ISBN
    List<Book> searchByIsbn(String isbn);
}
