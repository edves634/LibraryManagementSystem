package com.library.repositories.impl;

import com.library.database.entities.Book;
import com.library.exceptions.BookNotFoundException;
import com.library.exceptions.DatabaseException;
import com.library.repositories.BookRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Реализация репозитория для работы с книгами в базе данных.
 * Обеспечивает CRUD-операции для сущности Book.
 */
public class BookRepositoryImpl implements BookRepository {
    private final Connection connection; // Соединение с базой данных

    /**
     * Конструктор репозитория.
     * @param connection активное соединение с базой данных
     * @throws IllegalArgumentException если connection равен null
     * @throws IllegalStateException если соединение закрыто или недоступно
     */
    public BookRepositoryImpl(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("Подключение к базе данных не может быть null");
        }
        try {
            if (connection.isClosed()) {
                throw new IllegalStateException("Подключение уже закрыто");
            }
            this.connection = connection;

            // Проверка работоспособности соединения
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("SELECT 1");
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка инициализации BookRepository: " + e.getMessage(), e);
        }
    }

    /**
     * Получить все книги из базы данных.
     * @return список всех книг
     * @throws DatabaseException при ошибках работы с базой данных
     */
    @Override
    public List<Book> findAll() {
        String sql = "SELECT id, title, author, isbn, publication_year, available FROM books";
        List<Book> books = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                books.add(mapRowToBook(rs));
            }
            System.out.println("Найдено " + books.size() + " книг в базе данных");
        } catch (SQLException e) {
            throw new DatabaseException("Ошибка при поиске всех книг", e);
        }
        return books;
    }

    /**
     * Найти книгу по идентификатору.
     * @param id идентификатор книги
     * @return найденная книга
     * @throws BookNotFoundException если книга не найдена
     * @throws DatabaseException при ошибках работы с базой данных
     */
    @Override
    public Book findById(int id) throws BookNotFoundException {
        String sql = "SELECT id, title, author, isbn, publication_year, available FROM books WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            System.out.println("Выполнение запроса: " + stmt);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Book book = mapRowToBook(rs);
                    System.out.println("Найдена книга: " + book);
                    return book;
                } else {
                    throw new BookNotFoundException(id);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Ошибка при поиске книги по ID: " + id, e);
        }
    }

    /**
     * Удалить книгу из базы данных.
     * @param id идентификатор удаляемой книги
     * @throws BookNotFoundException если книга не найдена
     * @throws DatabaseException при ошибках работы с базой данных
     */
    @Override
    public void delete(int id) throws BookNotFoundException {
        String sql = "DELETE FROM books WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            System.out.println("Выполнение удаления: " + stmt);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new BookNotFoundException(id);
            }
            System.out.println("Удалено " + affectedRows + " книг(а)");
        } catch (SQLException e) {
            throw new DatabaseException("Ошибка при удалении книги с ID: " + id, e);
        }
    }

    /**
     * Сохранить новую книгу в базе данных.
     * @param book книга для сохранения
     * @throws DatabaseException при ошибках работы с базой данных
     */
    @Override
    public void save(Book book) {
        String sql = "INSERT INTO books (title, author, isbn, publication_year, available) VALUES (?, ?, ?, ?, ?)";

        System.out.println("Попытка сохранения книги: " + book);

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getIsbn());
            stmt.setInt(4, book.getPublicationYear());
            stmt.setBoolean(5, book.isAvailable());

            System.out.println("Выполнение SQL: " + stmt);

            int affectedRows = stmt.executeUpdate();
            System.out.println("Затронуто строк: " + affectedRows);

            if (affectedRows == 0) {
                throw new DatabaseException("Не удалось сохранить книгу, ни одна строка не изменена");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    book.setId(generatedKeys.getInt(1));
                    System.out.println("Сгенерированный ID: " + book.getId());
                } else {
                    System.out.println("Предупреждение: Не получен ID для сохраненной книги");
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Код ошибки: " + e.getErrorCode());
            throw new DatabaseException("Ошибка при сохранении книги: " + e.getMessage(), e);
        }
    }

    /**
     * Обновить информацию о книге в базе данных.
     * @param book книга с обновленными данными
     * @throws BookNotFoundException если книга не найдена
     * @throws DatabaseException при ошибках работы с базой данных
     */
    @Override
    public void update(Book book) throws BookNotFoundException {
        String sql = "UPDATE books SET title = ?, author = ?, isbn = ?, publication_year = ?, available = ? WHERE id = ?";

        System.out.println("Попытка обновления книги: " + book);

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getIsbn());
            stmt.setInt(4, book.getPublicationYear());
            stmt.setBoolean(5, book.isAvailable());
            stmt.setInt(6, book.getId());

            System.out.println("Выполнение обновления: " + stmt);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new BookNotFoundException(book.getId());
            }
            System.out.println("Обновлено " + affectedRows + " книг(а)");
        } catch (SQLException e) {
            throw new DatabaseException("Ошибка при обновлении книги с ID: " + book.getId(), e);
        }
    }

    /**
     * Преобразует строку результата запроса в объект Book.
     * @param rs ResultSet с данными книги
     * @return объект Book
     * @throws SQLException при ошибках чтения данных
     */
    private Book mapRowToBook(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setId(rs.getInt("id"));
        book.setTitle(rs.getString("Название"));
        book.setAuthor(rs.getString("Автор"));
        book.setIsbn(rs.getString("ISBN"));
        book.setPublicationYear(rs.getInt("Год публикации"));
        book.setAvailable(rs.getBoolean("Доступность"));
        return book;
    }

    /**
     * Проверяет существование книги с указанным ISBN в базе данных.
     * @param isbn ISBN для проверки
     * @return true если книга с таким ISBN существует, иначе false
     */
    public boolean isbnExists(String isbn) {
        String sql = "SELECT COUNT(*) FROM books WHERE isbn = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, isbn);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при проверке существования ISBN: " + e.getMessage());
        }
        return false;
    }
}
