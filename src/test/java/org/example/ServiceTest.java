package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServiceTest{

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private Service service;

    @BeforeEach
    void setUp() {
        // Инициализация аннотаций Mockito перед каждым тестом
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveUser_ValidUser_ShouldCallRepositoryUpdate() {
        User user = new User(1, "Updated User", "updated@user.com");
        service.saveUser(user);
        verify(userRepository, times(1)).save(user); // Проверяем, что save вызван один раз
    }

    @Test
    void saveUser_InvalidUser_ShouldThrowException() {
        User invalidUser = new User(0, null, null); // Некорректные данные
        assertThrows(IllegalArgumentException.class, () -> service.saveUser(invalidUser));
        verify(userRepository, never()).save(any(User.class)); // save не вызывается
    }

    @Test
    void getUserById_ExistingUser_ShouldReturnUser() {
        Long userId = 1L;
        User user = new User(1, "First User", "first@user.com");
        when(userRepository.findById(userId)).thenReturn(user);
        User result = service.getUserById(userId);
        assertNotNull(result);
        assertEquals("First User", result.getName());
        verify(userRepository, times(1)).findById(userId); // Проверяем вызов findById
    }

    @Test
    void getUserById_NonExistingUser_ShouldThrowException() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(null);
        assertThrows(RuntimeException.class, () -> service.getUserById(userId));
        verify(userRepository, times(1)).findById(userId); // Проверяем вызов findById
    }

    @Test
    void  getAllUsers_ShouldReturnUsersList() {
        List<User> users = Arrays.asList(
                new User(1, "First User", "first@user.com"),
                new User(2, "Second User", "second@user.com")
        );

        when(userRepository.findAll()).thenReturn(users);
        List<User> result = service.getAllUsers();
        assertEquals(2, result.size());
        assertEquals("First User", result.get(0).getName());
        verify(userRepository, times(1)).findAll(); // Проверяем вызов findAll
    }

    @Test
    void getAllUsers_NoUsers_ShouldReturnEmptyList() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<User> result = service.getAllUsers();

        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findAll(); // Проверяем вызов findAll
    }

    @Test
    void updateUser_ValidUser_ShouldCallRepositoryUpdate() {
        User user = new User(1, "Updated User", "updated@example.com");

        service.updateUser(user);

        verify(userRepository, times(1)).update(user); // Проверяем вызов update
    }

    @Test
    void updateUser_InvalidUser_ShouldThrowException() {
        User userWithoutId = new User(0, "Invalid User", "invalid@example.com");

        assertThrows(IllegalArgumentException.class, () -> service.updateUser(userWithoutId));

        verify(userRepository, never()).update(any(User.class)); // Убеждаемся, что update не вызывается
    }

    @Test
    void deleteUser_ShouldCallRepositoryDelete() {
        Long userId = 1L;

        service.deleteUser(userId);

        verify(userRepository, times(1)).delete(userId); // Проверяем вызов delete
    }
}