package org.example;

/**
 * Класс UserRepositoryRunner демонстрирует использование методов Service
 * для работы с базой данных.
 */
public class UserRepositoryRunner {
    public static void main(String[] args) {
        // Создание репозитория
        UserRepository userRepository = new UserRepository();

        // Создание сервиса
        Service service = new Service(userRepository);

        // Сохранение первого пользователя
        User user1 = new User(0, "User One", "user_one@example.com");
        service.saveUser(user1);

        // Сохранение второго пользователя
        User user2 = new User(0, "User Two", "user_two@example.com");
        service.saveUser(user2);

        // Вывод всех пользователей до удаления
        System.out.println("Пользователи до удаления:");
        service.getAllUsers().forEach(System.out::println);

        // Проверка поиска по ID
        User foundUser = service.getUserById((long) user1.getId());
        System.out.println("Найден пользователь по ID: " + foundUser);

        // Удаление первого пользователя
        service.deleteUser((long) user1.getId());
//        System.out.println("Пользователь с ID " + user1.getId() + " удалён.");

        // Вывод всех пользователей после удаления
        System.out.println("Пользователи после удаления:");
        service.getAllUsers().forEach(System.out::println);

        // Закрытие соединения
        userRepository.closeConnection();
    }
}