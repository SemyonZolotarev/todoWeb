package ru.zolotarev.todo.exceptions.task;

public class TaskDeadlineFormatException extends RuntimeException {
    public TaskDeadlineFormatException(String message) {
        super(message);
    }
}
