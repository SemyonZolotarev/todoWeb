package ru.zolotarev.todo.exceptions.task;

public class UserAlreadyExistException extends Exception{
    public UserAlreadyExistException() {
        super("Пользователь с таким именем уже существует.");
    }
}
