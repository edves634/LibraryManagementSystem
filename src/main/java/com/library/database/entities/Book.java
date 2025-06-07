package com.library.database.entities;

/**
 * Класс, представляющий книгу в библиотечной системе.
 * Содержит информацию о книге и методы для работы с ней.
 */
public class Book {
    private int id;                 // Уникальный идентификатор книги
    private String title;           // Название книги
    private String author;          // Автор книги
    private String isbn;            // ISBN книги
    private int publicationYear;    // Год публикации
    private boolean available;      // Флаг доступности книги (true - доступна)

    /**
     * Конструктор книги с обязательными параметрами.
     * @param title Название книги (не может быть пустым)
     * @param author Автор книги (не может быть пустым)
     * @param isbn ISBN книги (не может быть пустым)
     * @param publicationYear Год публикации (должен быть положительным числом)
     * @throws IllegalArgumentException если параметры не соответствуют требованиям
     */
    public Book(String title, String author, String isbn, int publicationYear) {
        this.setTitle(title);          // Установка названия с валидацией
        this.setAuthor(author);        // Установка автора с валидацией
        this.setIsbn(isbn);            // Установка ISBN с валидацией
        this.setPublicationYear(publicationYear); // Установка года с валидацией
        this.available = true;         // По умолчанию книга доступна
    }

    /**
     * Пустой конструктор для использования в ORM/фреймворках.
     */
    public Book() {
        // Пустой конструктор требуется для некоторых библиотек,
        // например, для работы с JDBC или ORM-фреймворками
    }

    /**
     * Устанавливает название книги с проверкой на валидность.
     * @param title Название книги (не может быть null или пустым)
     * @throws IllegalArgumentException если название невалидно
     */
    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Название не может быть пустым");
        }
        this.title = title.trim();  // Удаляем лишние пробелы
    }

    /**
     * Устанавливает автора книги с проверкой на валидность.
     * @param author Автор книги (не может быть null или пустым)
     * @throws IllegalArgumentException если автор невалиден
     */
    public void setAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Автор не может быть пустым");
        }
        this.author = author.trim();  // Удаляем лишние пробелы
    }

    /**
     * Устанавливает ISBN книги с проверкой на валидность.
     * @param isbn ISBN книги (не может быть null или пустым)
     * @throws IllegalArgumentException если ISBN невалиден
     */
    public void setIsbn(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN не может быть пустым");
        }
        this.isbn = isbn.trim();  // Удаляем лишние пробелы
    }

    /**
     * Устанавливает год публикации с проверкой на валидность.
     * @param year Год публикации (должен быть положительным числом)
     * @throws IllegalArgumentException если год невалиден
     */
    public void setPublicationYear(int year) {
        if (year <= 0) {
            throw new IllegalArgumentException("Год публикации должен быть положительным числом");
        }
        this.publicationYear = year;
    }

    // Стандартные геттеры и сеттеры

    public int getId() {
        return id;
    }

    /**
     * Устанавливает идентификатор книги.
     * @param id Уникальный числовой идентификатор
     */
    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    /**
     * Проверяет, доступна ли книга для выдачи.
     * @return true если книга доступна, false если книга выдана
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     * Устанавливает статус доступности книги.
     * @param available true - книга доступна, false - книга выдана
     */
    public void setAvailable(boolean available) {
        this.available = available;
    }
}


