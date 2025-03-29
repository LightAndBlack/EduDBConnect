package org.example;

import java.util.List;

/**
 * Класс Service
 * Представляет сервисный слой для управления объектами типа User.
 * Выполняет операции бизнес-логики и взаимодействует с репозиторием данных.
 */
public class Service {
    private final UserRepository userRepository;

    /**
     * Конструктор класса Service.
     * @param userRepository объект репозитория для работы с данными пользователей.
     */
    public Service(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Сохраняет нового пользователя.
     * Перед сохранением проверяет, что имя и email пользователя не пусты.
     * @param user объект пользователя, который нужно сохранить.
     * @throws IllegalArgumentException если имя или email пользователя равны null.
     */
    public void saveUser(User user) {
        if (user.getName() == null || user.getEmail() == null) {
            throw new IllegalArgumentException("Имя и email не могут быть пустыми");
        }
        userRepository.save(user);
    }

    /**
     * Получает пользователя по указанному ID.
     * Если пользователь с таким ID не найден, выбрасывает исключение.
     * @param id ID пользователя, которого нужно найти.
     * @return объект пользователя, найденный в репозитории.
     * @throws RuntimeException если пользователь с указанным ID не найден.
     */
    public User getUserById(Long id) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new RuntimeException("Пользователь с ID " + id + " не найден");
        }
        return user;
    }

    /**
     * Получает список всех пользователей.
     * @return список всех пользователей из репозитория.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Обновляет данные существующего пользователя.
     * Проверяет, что ID пользователя больше 0.
     * @param user объект пользователя, которого нужно обновить.
     * @throws IllegalArgumentException если ID пользователя <= 0.
     */
    public void updateUser(User user) {
        if (user.getId() <= 0) {
            throw new IllegalArgumentException("ID пользователя обязателен для обновления");
        }
        userRepository.update(user);
    }

    /**
     * Удаляет пользователя по указанному ID.
     * @param id ID пользователя, которого нужно удалить.
     */
    public void deleteUser(Long id) {
        userRepository.delete(id);
    }
}