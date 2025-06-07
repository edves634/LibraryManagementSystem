package com.library.controllers;

import com.library.database.entities.Book;
import com.library.exceptions.DatabaseException;
import com.library.services.BookService;
import java.util.List;
import java.util.Scanner;

/**
 * Контроллер для управления операциями с книгами
 * Обрабатывает пользовательский ввод и взаимодействует с BookService
 */
public class BookController {
    private final BookService bookService;  // Сервис для работы с книгами
    private final Scanner scanner;         // Для чтения пользовательского ввода

    /**
     * Конструктор контроллера
     * @param bookService сервис для работы с книгами
     */
    public BookController(BookService bookService) {
        this.bookService = bookService;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Отображает меню управления книгами и обрабатывает выбор пользователя
     */
    public void showBookMenu() {
        while (true) {
            // Вывод меню
            System.out.println("\n--- Управление книгами ---");
            System.out.println("1. Показать все книги");
            System.out.println("2. Добавить новую книгу");
            System.out.println("3. Обновить информацию о книге");
            System.out.println("4. Удалить книгу");
            System.out.println("5. Взять книгу в аренду");
            System.out.println("6. Вернуть книгу");
            System.out.println("7. Поиск книг");
            System.out.println("8. Вернуться в главное меню");
            System.out.print("Выберите опцию: ");

            // Обработка выбора пользователя
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Пожалуйста, введите корректное число!");
                continue;
            }

            // Выполнение действия в зависимости от выбора
            switch (choice) {
                case 1:
                    listAllBooks();
                    break;
                case 2:
                    addNewBook();
                    break;
                case 3:
                    updateBook();
                    break;
                case 4:
                    deleteBook();
                    break;
                case 5:
                    borrowBook();
                    break;
                case 6:
                    returnBook();
                    break;
                case 7:
                    searchBooks();
                    break;
                case 8:
                    return;  // Выход в главное меню
                default:
                    System.out.println("Неверная опция, попробуйте снова.");
            }
        }
    }

    /**
     * Выводит список всех книг в библиотеке
     */
    private void listAllBooks() {
        System.out.println("\nВсе книги:");
        List<Book> books = bookService.getAllBooks();
        if (books.isEmpty()) {
            System.out.println("В библиотеке нет книг.");
        } else {
            // Форматированный вывод информации о книгах
            System.out.println("ID | Название | Автор | ISBN | Год | Статус");
            System.out.println("------------------------------------------");
            books.forEach(book -> {
                String status = book.isAvailable() ? "Доступна" : "В аренде";
                System.out.printf("%d | %s | %s | %s | %d | %s%n",
                        book.getId(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getIsbn(),
                        book.getPublicationYear(),
                        status);
            });
        }
    }

    /**
     * Добавляет новую книгу в библиотеку
     * Запрашивает у пользователя данные книги и обрабатывает их
     */
    public void addNewBook() {
        try {
            System.out.println("\nДобавление новой книги");

            // Ввод данных книги
            System.out.print("Введите название книги: ");
            String title = scanner.nextLine();

            System.out.print("Введите автора: ");
            String author = scanner.nextLine();

            System.out.print("Введите ISBN: ");
            String isbn = scanner.nextLine();

            System.out.print("Введите год публикации: ");
            int year = Integer.parseInt(scanner.nextLine());

            // Создание и сохранение книги
            Book book = new Book(title, author, isbn, year);
            bookService.addBook(book);
            System.out.println("Книга успешно добавлена! ID: " + book.getId());
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка ввода: " + e.getMessage());
        } catch (DatabaseException e) {
            System.out.println("Ошибка базы данных: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Неожиданная ошибка: " + e.getMessage());
        }
    }

    /**
     * Обновляет информацию о существующей книге
     */
    private void updateBook() {
        System.out.println("\nОбновление информации о книге");
        System.out.print("Введите ID книги для обновления: ");

        // Получение ID книги
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Неверный формат ID.");
            return;
        }

        // Поиск книги
        Book existingBook = bookService.getBookById(id);
        if (existingBook == null) {
            System.out.println("Книга с ID " + id + " не найдена.");
            return;
        }

        // Вывод текущей информации
        System.out.println("Текущая информация о книге:");
        System.out.println("Название: " + existingBook.getTitle());
        System.out.println("Автор: " + existingBook.getAuthor());
        System.out.println("ISBN: " + existingBook.getIsbn());
        System.out.println("Год: " + existingBook.getPublicationYear());

        // Ввод новых данных (пустые строки означают сохранение текущих значений)
        System.out.print("Введите новое название (оставьте пустым для сохранения текущего): ");
        String newTitle = scanner.nextLine();

        System.out.print("Введите нового автора (оставьте пустым для сохранения текущего): ");
        String newAuthor = scanner.nextLine();

        System.out.print("Введите новый ISBN (оставьте пустым для сохранения текущего): ");
        String newIsbn = scanner.nextLine();

        System.out.print("Введите новый год публикации (0 для сохранения текущего): ");
        int newYear;
        try {
            newYear = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Неверный формат года. Сохранено текущее значение.");
            newYear = 0;
        }

        // Обновление данных книги
        if (!newTitle.isEmpty()) existingBook.setTitle(newTitle);
        if (!newAuthor.isEmpty()) existingBook.setAuthor(newAuthor);
        if (!newIsbn.isEmpty()) existingBook.setIsbn(newIsbn);
        if (newYear > 0) existingBook.setPublicationYear(newYear);

        // Сохранение изменений
        bookService.updateBook(existingBook);
        System.out.println("Информация о книге успешно обновлена!");
    }

    /**
     * Удаляет книгу из библиотеки после подтверждения
     */
    private void deleteBook() {
        System.out.println("\nУдаление книги");
        System.out.print("Введите ID книги для удаления: ");

        // Получение ID книги
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Неверный формат ID.");
            return;
        }

        // Поиск книги
        Book book = bookService.getBookById(id);
        if (book == null) {
            System.out.println("Книга с ID " + id + " не найдена.");
            return;
        }

        // Подтверждение удаления
        System.out.println("Вы собираетесь удалить:");
        System.out.println("Название: " + book.getTitle());
        System.out.println("Автор: " + book.getAuthor());
        System.out.print("Вы уверены? (yes/no): ");

        String confirmation = scanner.nextLine();
        if (confirmation.equalsIgnoreCase("yes")) {
            bookService.deleteBook(id);
            System.out.println("Книга успешно удалена!");
        } else {
            System.out.println("Удаление отменено.");
        }
    }

    /**
     * Обрабатывает операцию взятия книги в аренду
     */
    private void borrowBook() {
        System.out.println("\nАренда книги");

        // Ввод ID книги
        System.out.print("Введите ID книги: ");
        int bookId;
        try {
            bookId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Неверный формат ID книги.");
            return;
        }

        // Ввод ID пользователя
        System.out.print("Введите ID пользователя: ");
        int userId;
        try {
            userId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Неверный формат ID пользователя.");
            return;
        }

        // Выполнение операции аренды
        try {
            bookService.borrowBook(bookId, userId);
            System.out.println("Книга успешно взята в аренду!");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    /**
     * Обрабатывает операцию возврата книги
     */
    private void returnBook() {
        System.out.println("\nВозврат книги");
        System.out.print("Введите ID книги: ");

        // Ввод ID книги
        int bookId;
        try {
            bookId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Неверный формат ID книги.");
            return;
        }

        // Выполнение операции возврата
        try {
            bookService.returnBook(bookId);
            System.out.println("Книга успешно возвращена!");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    /**
     * Осуществляет поиск книг по различным критериям
     */
    private void searchBooks() {
        System.out.println("\nПоиск книг");
        System.out.println("1. Поиск по названию");
        System.out.println("2. Поиск по автору");
        System.out.println("3. Поиск по ISBN");
        System.out.print("Выберите вариант поиска: ");

        // Выбор критерия поиска
        int option;
        try {
            option = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Неверный вариант.");
            return;
        }

        String searchTerm;
        List<Book> results;

        // Выполнение поиска в зависимости от выбранного критерия
        switch (option) {
            case 1:
                System.out.print("Введите название для поиска: ");
                searchTerm = scanner.nextLine();
                results = bookService.searchByTitle(searchTerm);
                break;
            case 2:
                System.out.print("Введите автора для поиска: ");
                searchTerm = scanner.nextLine();
                results = bookService.searchByAuthor(searchTerm);
                break;
            case 3:
                System.out.print("Введите ISBN для поиска: ");
                searchTerm = scanner.nextLine();
                results = bookService.searchByIsbn(searchTerm);
                break;
            default:
                System.out.println("Неверный вариант поиска.");
                return;
        }

        // Вывод результатов поиска
        if (results.isEmpty()) {
            System.out.println("Книги по вашему запросу не найдены.");
        } else {
            System.out.println("\nРезультаты поиска:");
            System.out.println("ID | Название | Автор | ISBN | Год | Статус");
            System.out.println("------------------------------------------");
            results.forEach(book -> {
                String status = book.isAvailable() ? "Доступна" : "В аренде";
                System.out.printf("%d | %s | %s | %s | %d | %s%n",
                        book.getId(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getIsbn(),
                        book.getPublicationYear(),
                        status);
            });
        }
    }
}
