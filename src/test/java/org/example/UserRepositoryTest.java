package org.example;

import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {

    private static Connection connection;  // Единое соединение для всех тестов
    private static UserRepository userRepository;  // Единый репозиторий

    @BeforeAll
    static void setUpDatabase() throws Exception {
        // Подключение к базе данных один раз
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");

        // Создание таблицы перед всеми тестами
        try (Statement statement = connection.createStatement()) {
            String createTableSQL = """
                CREATE TABLE IF NOT EXISTS Users (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    name VARCHAR(255) NOT NULL,
                    email VARCHAR(255) NOT NULL
                );
            """;
            statement.execute(createTableSQL);
        }

        // Инициализация репозитория
        userRepository = new UserRepository(connection);
    }

    @AfterAll
    static void tearDownDatabase() throws Exception {
        // Удаление таблицы и закрытие соединения после всех тестов
        try (Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS Users;");
        }
        connection.close();
    }

    @BeforeEach
    void clearTable() throws Exception {
        // Очистка данных в таблице перед каждым тестом
        try (Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM Users;");
        }
    }

    @Test
    void save_ShouldInsertUserIntoDatabase() {
        // Arrange
        User user = new User(0, "John Doe", "john.doe@example.com");

        // Act
        userRepository.save(user);
        User retrievedUser = userRepository.findById((long) user.getId());

        // Assert
        assertNotNull(retrievedUser, "Пользователь должен быть найден в базе данных");
        System.out.println("Найден пользователь с ID: " + retrievedUser.getId());
        assertEquals("John Doe", retrievedUser.getName());
        assertEquals("john.doe@example.com", retrievedUser.getEmail());
    }

    @Test
    void findById_ShouldReturnCorrectUser() {
        // Arrange
        User user = new User(0, "Alice Wonderland", "alice@example.com");
        userRepository.save(user);

        // Act
        User retrievedUser = userRepository.findById((long) user.getId());

        // Assert
        assertNotNull(retrievedUser, "Пользователь должен быть найден");
        System.out.println("Найден пользователь с ID: " + retrievedUser.getId());
        assertEquals("Alice Wonderland", retrievedUser.getName());
        assertEquals("alice@example.com", retrievedUser.getEmail());
    }

    @Test
    void findAll_ShouldReturnAllUsers() {
        // Arrange
        User user1 = new User(0, "User One", "user_one@example.com");
        User user2 = new User(0, "User Two", "user_two@example.com");
        userRepository.save(user1);
        userRepository.save(user2);

        // Act
        List<User> users = userRepository.findAll();

        // Assert
        assertEquals(2, users.size(), "Должно быть найдено два пользователя");
        users.forEach(user -> System.out.println("Найден пользователь с ID: " + user.getId()));
        assertEquals("User One", users.get(0).getName());
        assertEquals("User Two", users.get(1).getName());
    }

    @Test
    void update_ShouldModifyExistingUser() {
        // Arrange
        User user = new User(0, "Old Name", "old.email@example.com");
        userRepository.save(user);

        // Act
        user.setName("Updated Name");
        user.setEmail("updated.email@example.com");
        userRepository.update(user);
        User updatedUser = userRepository.findById((long) user.getId());

        // Assert
        assertNotNull(updatedUser, "Обновлённый пользователь должен быть найден");
//        System.out.println("Обновлён пользователь с ID: " + updatedUser.getId());
        assertEquals("Updated Name", updatedUser.getName());
        assertEquals("updated.email@example.com", updatedUser.getEmail());
    }

    @Test
    void delete_ShouldRemoveUserFromDatabase() {
        // Arrange
        User user = new User(0, "User To Delete", "delete@example.com");
        userRepository.save(user);

        // Act
        userRepository.delete((long) user.getId());
        User deletedUser = userRepository.findById((long) user.getId());

        // Assert
        assertNull(deletedUser, "Удалённый пользователь не должен быть найден");
        System.out.println("Пользователь с ID " + user.getId() + " был удалён.");
    }

    @Test
    void findById_ShouldReturnNullForNonexistentUser() {
        // Act
        User retrievedUser = userRepository.findById(999L);

        // Assert
        assertNull(retrievedUser, "Метод должен вернуть null для несуществующего пользователя");
        System.out.println("Пользователь с ID 999 не найден.");
    }
}