package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *  Класс UserRepository отвечает за взаимодействие с БД
*/
public class UserRepository {
    private static final String URL = "jdbc:h2:mem:test_db;INIT=RUNSCRIPT FROM 'classpath:schema.sql'";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    private final Connection connection;

    // Конструктор с передачей существующего соединения
    public UserRepository(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("Connection не может быть null");
        }
        this.connection = connection;
        System.out.println("UserRepository: Используем существующее соединение");
    }

    // Конструктор, устанавливающий соединения
    public UserRepository() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Соединение с БД установлено");
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка подключения к БД: " + e.getMessage(), e);
        }
    }

    // Закрытие соединения
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Соединение с БД закрыто");
            }
        } catch (SQLException e) {
            System.out.println("Ошибка закрытия соединения с БД: " + e.getMessage());
        }
    }

    public void save(User user) {
        String sql = "INSERT INTO Users (name, email) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.executeUpdate();

            // Извлекаю автоматически сгенерированный ключ (id) после вставки данных и присваиваю его объекту пользователя.
            // Обрабатываю результат, если ключ был успешно получен.
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                    System.out.println("Пользователь сохранён с ID: " + user.getId());
                }
            }
        } catch (SQLException e) {
            System.out.println("Ошибка сохранения пользователя: " + e.getMessage());
        }
    }

    // Поиск пользователя по ID
    public User findById(Long id) {
        String sql = "SELECT * FROM Users WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new User(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("email")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Ошибка поиска пользователя с ID: " + id + ". " + e.getMessage());
        }
        System.out.println("Пользователь с ID " + id + " не найден");
        return null;
    }

    // Получение всех пользователей
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM Users";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                users.add(new User(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Ошибка извлечения всех пользователей: " + e.getMessage());
        }
        return users;
    }

    // Обновление данных пользователя
    public void update(User user) {
        String sql = "UPDATE Users SET name = ?, email = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setInt(3, user.getId());
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Пользователь с ID " + user.getId() + " обновлён");
            } else {
                System.out.println("Пользователь с ID " + user.getId() + " не найден для обновления");
            }
        } catch (SQLException e) {
            System.out.println("Ошибка обновления пользователя: " + e.getMessage());
        }
    }

    // Удаление пользователя
    public void delete(Long id) {
        String sql = "DELETE FROM Users WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Пользователь с ID " + id + " удалён");
            } else {
                System.out.println("Пользователь с ID " + id + " не найден для удаления");
            }
        } catch (SQLException e) {
            System.out.println("Ошибка удаления пользователя: " + e.getMessage());
        }
    }
}
