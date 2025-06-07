package com.library.database.entities;

/**
 * Класс, представляющий пользователя библиотечной системы.
 * Содержит основную информацию о пользователе библиотеки.
 */
public class User {
    private int id;         // Уникальный идентификатор пользователя
    private String name;    // Имя пользователя
    private String email;   // Электронная почта пользователя

    /**
     * Конструктор по умолчанию.
     * Требуется для работы ORM-фреймворков и сериализации.
     */
    public User() {
    }

    /**
     * Конструктор с параметрами для создания объекта пользователя.
     * @param id уникальный идентификатор пользователя
     * @param name имя пользователя
     * @param email электронная почта пользователя
     */
    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    /**
     * Возвращает идентификатор пользователя.
     * @return числовой идентификатор пользователя
     */
    public int getId() {
        return id;
    }

    /**
     * Устанавливает идентификатор пользователя.
     * @param id числовой идентификатор пользователя
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Возвращает имя пользователя.
     * @return строка с именем пользователя
     */
    public String getName() {
        return name;
    }

    /**
     * Устанавливает имя пользователя.
     * @param name строка с именем пользователя
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Возвращает электронную почту пользователя.
     * @return строка с email пользователя
     */
    public String getEmail() {
        return email;
    }

    /**
     * Устанавливает электронную почту пользователя.
     * @param email строка с email пользователя
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
