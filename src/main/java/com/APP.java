package com;

import com.library.config.DatabaseConfig;
import com.library.controllers.BookController;
import com.library.controllers.UserController;
import com.library.database.LibraryDatabase;
import com.library.repositories.BookRepository;
import com.library.repositories.UserRepository;
import com.library.repositories.impl.BookRepositoryImpl;
import com.library.repositories.impl.UserRepositoryImpl;
import com.library.services.BookService;
import com.library.services.UserService;
import com.library.services.impl.BookServiceImpl;
import com.library.services.impl.UserServiceImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class APP {
    public static void main(String[] args) {
        Connection connection = null;
        try {
            // 1. Инициализация конфигурации базы данных и подключения
            DatabaseConfig config = new DatabaseConfig();
            LibraryDatabase database = new LibraryDatabase(config);

            // 2. Установка соединения с базой данных
            connection = database.getConnection();
            if (connection == null || connection.isClosed()) {
                System.err.println("Не удалось установить соединение с базой данных");
                return;
            }

            // 3. Создание репозиториев для работы с данными
            BookRepository bookRepository = new BookRepositoryImpl(connection);
            UserRepository userRepository = new UserRepositoryImpl(connection);

            // 4. Создание сервисов
            BookService bookService = new BookServiceImpl(bookRepository);
            UserService userService = new UserServiceImpl(userRepository);

            // 5. Создание контроллеров для обработки пользовательских запросов
            BookController bookController = new BookController(bookService);
            UserController userController = new UserController(userService);

            // 6. Запуск главного меню приложения
            showMainMenu(bookController, userController);

        } catch (SQLException e) {
            System.err.println("Ошибка базы данных: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Ошибка приложения: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // 7. Закрытие соединения с базой данных при завершении работы
            if (connection != null) {
                try {
                    if (!connection.isClosed()) {
                        connection.close();
                        System.out.println("Соединение с базой данных успешно закрыто");
                    }
                } catch (SQLException e) {
                    System.err.println("Не удалось закрыть соединение с базой данных: " + e.getMessage());
                }
            }
        }
    }

    private static void showMainMenu(BookController bookController, UserController userController) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Система управления библиотекой ===");
            System.out.println("1. Управление книгами");
            System.out.println("2. Управление пользователями");
            System.out.println("3. Выход");
            System.out.print("Выберите опцию: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        bookController.showBookMenu();
                        break;
                    case 2:
                        userController.showUserMenu();
                        break;
                    case 3:
                        System.out.println("Завершение работы приложения...");
                        return;
                    default:
                        System.out.println("Неверная опция! Пожалуйста, попробуйте снова.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: пожалуйста, введите число от 1 до 3");
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}