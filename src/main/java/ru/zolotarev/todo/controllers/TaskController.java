package ru.zolotarev.todo.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.zolotarev.todo.dto.TaskDTO;
import ru.zolotarev.todo.entities.TaskEntity;
import ru.zolotarev.todo.enums.SortByDeadlineMethods;
import ru.zolotarev.todo.enums.TaskFields;
import ru.zolotarev.todo.enums.TaskStatus;
import ru.zolotarev.todo.exceptions.task.TaskNotFoundException;
import ru.zolotarev.todo.exceptions.user.UserNotFoundException;
import ru.zolotarev.todo.services.TaskService;

@RestController
@RequestMapping("/api/v1.2/users/{userId}/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping()
    public ResponseEntity<?> createTask(@RequestBody TaskDTO taskDTO,
                                        @PathVariable Long userId) {
        try {
            return ResponseEntity.ok(taskService.createTask(taskDTO, userId));
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка создания задачи.");
        }
    }

    @PutMapping("/{field}")
    public ResponseEntity<?> changeTask(@RequestBody TaskDTO taskDTO,
                                        @PathVariable Long userId,
                                        @PathVariable TaskFields field) {

        try {
            return ResponseEntity.ok(taskService.changeTask(taskDTO, userId, field));
        } catch (UserNotFoundException | TaskNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка редактирования задачи.");
        }
    }

    @GetMapping()
    public ResponseEntity<?> allTasks(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(taskService.showAllTasks(userId));
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка поиска задач по id.");
        }
    }

    @GetMapping("/{status}")
    public ResponseEntity<?> filterTasksByStatus(@PathVariable Long userId,
                                                 @PathVariable TaskStatus status) {
        try {
            return ResponseEntity.ok(taskService.filterTasksByStatus(userId, status));
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка фильтрации задач.");
        }
    }

    @GetMapping("/status")
    public ResponseEntity<?> sortTasksByStatus(@PathVariable Long userId,
                                               @RequestParam String first,
                                               @RequestParam String second,
                                               @RequestParam String third) {
        try {
            return ResponseEntity.ok(taskService.sortTasksByStatus(userId, first, second, third));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка сортировки задач по статусу.");
        }
    }

    @GetMapping("/deadline")
    public ResponseEntity<?> tasksByDeadline(@PathVariable Long userId,
                                             @RequestParam SortByDeadlineMethods methods) {
        try {
            return ResponseEntity.ok(taskService.sortTaskByDeadline(userId, methods));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка сортировки задач по дедлайну.");
        }

    }
}


