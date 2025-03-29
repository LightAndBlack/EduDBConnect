package org.example;

/**
 * Класс User представляет сущность пользователя, хранимую в БД
*/
public class User {
    private int id; // Уникальный идентификатор пользователя
    private String name; // Имя пользователя
    private String email; // Электронная почта пользователя

    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name = '" + name + '\'' +
                ", email = '" + email + '\'' +
                '}';
    }
}
