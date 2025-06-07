package com.library.exceptions;

/**
 * Специализированное исключение, выбрасываемое при попытке
 * доступа к несуществующей книге в библиотечной системе.
 * Содержит идентификатор книги, которая не была найдена.
 */
public class BookNotFoundException extends RuntimeException {
  private final int bookId;  // ID книги, которая не была найдена

  /**
   * Создает исключение с ID не найденной книги.
   * Сообщение формируется автоматически.
   * @param bookId ID книги, которая не была найдена
   */
  public BookNotFoundException(int bookId) {
    super("Книга с ID " + bookId + " не найдена");
    this.bookId = bookId;
  }

  /**
   * Создает исключение с ID книги и причиной ошибки.
   * @param bookId ID книги, которая не была найдена
   * @param cause исходное исключение, которое стало причиной
   */
  public BookNotFoundException(int bookId, Throwable cause) {
    super("Книга с ID " + bookId + " не найдена", cause);
    this.bookId = bookId;
  }

  /**
   * Создает исключение с кастомным сообщением и ID книги.
   * @param message детализированное сообщение об ошибке
   * @param bookId ID книги, которая не была найдена
   */
  public BookNotFoundException(String message, int bookId) {
    super(message);
    this.bookId = bookId;
  }

  /**
   * Создает исключение с кастомным сообщением, ID книги и причиной.
   * @param message детализированное сообщение об ошибке
   * @param bookId ID книги, которая не была найдена
   * @param cause исходное исключение, которое стало причиной
   */
  public BookNotFoundException(String message, int bookId, Throwable cause) {
    super(message, cause);
    this.bookId = bookId;
  }

  /**
   * Возвращает ID книги, которая не была найдена.
   * @return числовой ID не найденной книги
   */
  public int getBookId() {
    return bookId;
  }
}
