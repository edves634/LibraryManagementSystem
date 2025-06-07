package com.library.controllers;

import com.library.database.entities.User;
import com.library.services.UserService;
import java.util.List;
import java.util.Scanner;

/**
 * Контроллер для управления пользователями библиотеки.
 * Обрабатывает пользовательский ввод и взаимодействует с UserService.
 */
public class UserController {
    private final UserService userService;  // Сервис для работы с пользователями
    private final Scanner scanner;         // Для чтения пользовательского ввода

    /**
     * Конструктор контроллера пользователей.
     * @param userService сервис для работы с пользователями
     */
    public UserController(UserService userService) {
        this.userService = userService;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Отображает меню управления пользователями и обрабатывает выбор пользователя.
     */
    public void showUserMenu() {
        while (true) {
            // Вывод меню управления пользователями
            System.out.println("\n--- Управление пользователями ---");
            System.out.println("1. Список всех пользователей");
            System.out.println("2. Добавить нового пользователя");
            System.out.println("3. Обновить данные пользователя");
            System.out.println("4. Удалить пользователя");
            System.out.println("5. Вернуться в главное меню");
            System.out.print("Выберите действие: ");

            try {
                // Обработка выбора пользователя
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        listAllUsers();
                        break;
                    case 2:
                        addNewUser();
                        break;
                    case 3:
                        updateUser();
                        break;
                    case 4:
                        deleteUser();
                        break;
                    case 5:
                        return;  // Выход в главное меню
                    default:
                        System.out.println("Неверный выбор, попробуйте снова.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите число от 1 до 5");
            }
        }
    }

    /**
     * Выводит список всех зарегистрированных пользователей.
     */
    private void listAllUsers() {
        System.out.println("\nСписок пользователей:");
        List<User> users = userService.getAllUsers();

        if (users.isEmpty()) {
            System.out.println("Пользователи не найдены.");
        } else {
            // Форматированный вывод информации о пользователях
            users.forEach(user ->
                    System.out.printf("ID: %d | Имя: %s | Email: %s%n",
                            user.getId(), user.getName(), user.getEmail()));
        }
    }

    /**
     * Добавляет нового пользователя в систему.
     * Запрашивает имя и email, выполняет базовую валидацию.
     */
    private void addNewUser() {
        System.out.println("\nДобавление нового пользователя");

        // Ввод и валидация имени
        System.out.print("Введите имя пользователя: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Ошибка: имя не может быть пустым");
            return;
        }

        // Ввод и валидация email
        System.out.print("Введите email пользователя: ");
        String email = scanner.nextLine().trim();
        if (email.isEmpty() || !email.contains("@")) {
            System.out.println("Ошибка: введите корректный email");
            return;
        }

        // Создание и сохранение пользователя
        User newUser = new User();
        newUser.setName(name);
        newUser.setEmail(email);

        try {
            userService.addUser(newUser);
            System.out.println("Пользователь успешно добавлен!");
        } catch (Exception e) {
            System.out.println("Ошибка при добавлении: " + e.getMessage());
        }
    }

    /**
     * Обновляет данные существующего пользователя.
     * Позволяет изменить имя и/или email.
     */
    private void updateUser() {
        System.out.println("\nОбновление данных пользователя");
        System.out.print("Введите ID пользователя: ");

        try {
            // Получение ID пользователя
            int id = Integer.parseInt(scanner.nextLine());
            User existingUser = userService.getUserById(id);

            if (existingUser == null) {
                System.out.println("Пользователь с ID " + id + " не найден.");
                return;
            }

            // Вывод текущей информации
            System.out.println("Текущие данные:");
            System.out.println("Имя: " + existingUser.getName());
            System.out.println("Email: " + existingUser.getEmail());

            // Ввод новых данных (пустые строки означают сохранение текущих значений)
            System.out.print("Введите новое имя (оставьте пустым для сохранения текущего): ");
            String newName = scanner.nextLine().trim();

            System.out.print("Введите новый email (оставьте пустым для сохранения текущего): ");
            String newEmail = scanner.nextLine().trim();

            // Обновление данных
            if (!newName.isEmpty()) {
                existingUser.setName(newName);
            }

            if (!newEmail.isEmpty()) {
                if (!newEmail.contains("@")) {
                    System.out.println("Ошибка: некорректный email");
                    return;
                }
                existingUser.setEmail(newEmail);
            }

            // Сохранение изменений
            userService.updateUser(existingUser);
            System.out.println("Данные пользователя обновлены!");
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите числовой ID");
        } catch (Exception e) {
            System.out.println("Ошибка при обновлении: " + e.getMessage());
        }
    }

    /**
     * Удаляет пользователя из системы после подтверждения.
     */
    private void deleteUser() {
        System.out.println("\nУдаление пользователя");
        System.out.print("Введите ID пользователя: ");

        try {
            // Получение ID пользователя
            int id = Integer.parseInt(scanner.nextLine());
            User user = userService.getUserById(id);

            if (user == null) {
                System.out.println("Пользователь с ID " + id + " не найден.");
                return;
            }

            // Подтверждение удаления
            System.out.println("Вы собираетесь удалить:");
            System.out.println("Имя: " + user.getName());
            System.out.println("Email: " + user.getEmail());
            System.out.print("Подтвердите удаление (да/нет): ");

            String confirmation = scanner.nextLine().trim();
            if (confirmation.equalsIgnoreCase("да")) {
                userService.deleteUser(id);
                System.out.println("Пользователь успешно удален!");
            } else {
                System.out.println("Удаление отменено.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите числовой ID");
        } catch (Exception e) {
            System.out.println("Ошибка при удалении: " + e.getMessage());
        }
    }
}
