Описание проекта: Библиотечная система (консольное приложение)

1. Рабочее окружение:

Язык: Java

СУБД: SQLite (встроенная, используется файл library.db)

Сборка: Maven

Архитектура: Многослойная с соблюдением SOLID

Основные компоненты:

A. Сущности:

Book: Книга (id, название, автор, ISBN, год, доступность)

User: Пользователь (id, имя, email)

B. Репозитории:

BookRepository/BookRepositoryImpl: CRUD для книг + поиск

UserRepository/UserRepositoryImpl: CRUD для пользователей

C. Сервисы:

BookService/BookServiceImpl: Логика работы с книгами (аренда/возврат)

UserService/UserServiceImpl: Управление пользователями

NotificationService: Отправка уведомлений (заглушка)

D. Контроллеры:

BookController: Консольный интерфейс для книг

UserController: Консольный интерфейс для пользователей

4. Особенности реализации:

Принципы SOLID:

Single Responsibility:

Каждый класс имеет одну ответственность (например, BookRepository - только работа с БД)

Open-Closed:

Интерфейсы (BookService) позволяют расширять функционал без изменения существующего кода

Liskov Substitution:

Все реализации следуют контрактам интерфейсов

Interface Segregation:

Узкоспециализированные интерфейсы (например, отдельно NotificationService)

Dependency Inversion:

Зависимости передаются через конструкторы (BookServiceImpl(BookRepository))

5. Работа с БД:

Используется JDBC + SQLite

Автоматическое создание таблиц при старте

Транзакции обрабатываются на уровне сервисов

Репозитории бросают кастомные исключения (BookNotFoundException)

6. Примеры сценариев:

Добавление книги с проверкой уникальности ISBN

Аренда книги с проверкой доступности

Поиск книг по различным критериям

Управление пользователями

7. Запуск:

Инициализация БД (LibraryDatabase)

Создание цепочки зависимостей:

java
BookRepository bookRepo = new BookRepositoryImpl(connection);
BookService bookService = new BookServiceImpl(bookRepo);
BookController bookController = new BookController(bookService);
Запуск главного меню

8. Дальнейшее развитие:

Добавление авторизации

Журналирование действий

Расширенная система уведомлений (email/SMS)

Миграция на Spring Framework
