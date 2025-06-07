package com.library.services.impl;

import com.library.database.entities.Book;
import com.library.exceptions.BookNotFoundException;
import com.library.exceptions.DatabaseException;
import com.library.repositories.BookRepository;
import com.library.services.BookService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация сервиса для работы с книгами.
 * Обеспечивает бизнес-логику работы с книгами библиотеки.
 */
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;  // Репозиторий для работы с книгами

    /**
     * Конструктор сервиса.
     * @param bookRepository репозиторий книг (не может быть null)
     */
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Получить список всех книг.
     * @return список всех книг в библиотеке
     */
    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * Найти книгу по идентификатору.
     * @param id идентификатор книги
     * @return найденная книга
     * @throws RuntimeException если книга не найдена
     */
    @Override
    public Book getBookById(int id) {
        try {
            return bookRepository.findById(id);
        } catch (BookNotFoundException e) {
            throw new RuntimeException("Книга с ID " + id + " не найдена", e);
        }
    }

    /**
     * Добавить новую книгу.
     * @param book книга для добавления
     * @throws DatabaseException если книга с таким ISBN уже существует
     */
    @Override
    public void addBook(Book book) throws DatabaseException {
        // Проверяем, существует ли книга с таким ISBN
        List<Book> existingBooks = searchByIsbn(book.getIsbn());
        if (!existingBooks.isEmpty()) {
            throw new DatabaseException("Книга с ISBN " + book.getIsbn() + " уже существует");
        }
        bookRepository.save(book);
    }

    /**
     * Обновить информацию о книге.
     * @param book книга с обновленными данными
     * @throws RuntimeException если книга не найдена
     */
    @Override
    public void updateBook(Book book) {
        try {
            bookRepository.update(book);
        } catch (BookNotFoundException e) {
            throw new RuntimeException("Не удалось обновить книгу", e);
        }
    }

    /**
     * Удалить книгу.
     * @param id идентификатор удаляемой книги
     * @throws RuntimeException если книга не найдена
     */
    @Override
    public void deleteBook(int id) {
        try {
            bookRepository.delete(id);
        } catch (BookNotFoundException e) {
            throw new RuntimeException("Не удалось удалить книгу", e);
        }
    }

    /**
     * Поиск книг по названию (регистронезависимый).
     * @param title часть названия для поиска
     * @return список найденных книг
     */
    @Override
    public List<Book> searchByTitle(String title) {
        return bookRepository.findAll().stream()
                .filter(b -> b.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Поиск книг по автору (регистронезависимый).
     * @param author часть имени автора для поиска
     * @return список найденных книг
     */
    @Override
    public List<Book> searchByAuthor(String author) {
        return bookRepository.findAll().stream()
                .filter(b -> b.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Поиск книг по ISBN (регистронезависимый).
     * @param isbn ISBN для поиска
     * @return список найденных книг
     */
    @Override
    public List<Book> searchByIsbn(String isbn) {
        return bookRepository.findAll().stream()
                .filter(b -> b.getIsbn().equalsIgnoreCase(isbn))
                .collect(Collectors.toList());
    }

    /**
     * Взять книгу в аренду.
     * @param bookId идентификатор книги
     * @param userId идентификатор пользователя
     * @throws RuntimeException если книга не найдена или уже взята
     */
    @Override
    public void borrowBook(int bookId, int userId) {
        try {
            Book book = bookRepository.findById(bookId);
            if (!book.isAvailable()) {
                throw new IllegalStateException("Книга уже взята в аренду");
            }

            book.setAvailable(false);
            bookRepository.update(book);
        } catch (BookNotFoundException e) {
            throw new RuntimeException("Книга с ID " + bookId + " не найдена", e);
        }
    }

    /**
     * Вернуть книгу в библиотеку.
     * @param bookId идентификатор книги
     * @throws RuntimeException если книга не найдена или не была взята
     */
    @Override
    public void returnBook(int bookId) {
        try {
            Book book = bookRepository.findById(bookId);
            if (book.isAvailable()) {
                throw new IllegalStateException("Книга не была взята в аренду");
            }

            book.setAvailable(true);
            bookRepository.update(book);
        } catch (BookNotFoundException e) {
            throw new RuntimeException("Книга с ID " + bookId + " не найдена", e);
        }
    }
}