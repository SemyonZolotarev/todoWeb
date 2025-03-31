package ru.zolotarev.todo.exceptions.user;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("Пользователь с id: '" + id + "' не найден.");
    }

    public UserNotFoundException(String email) {
        super("Пользователь с email: '" + email + "' не найден.");
    }
}
