package ru.zolotarev.todo.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.zolotarev.todo.dto.TaskDTO;
import ru.zolotarev.todo.enums.SortByDeadlineMethods;
import ru.zolotarev.todo.enums.TaskFields;
import ru.zolotarev.todo.enums.TaskStatus;
import ru.zolotarev.todo.services.TaskService;

@RestController
@RequestMapping("/api/v1.3/users/{userId}/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping()
    public ResponseEntity<?> createTask(@RequestBody TaskDTO taskDTO,
                                        @PathVariable Long userId) {
        try {
            return ResponseEntity.ok(taskService.createTask(taskDTO, userId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка создания задачи.");
        }
    }

    @PatchMapping("/{field}")
    public ResponseEntity<?> changeTask(@RequestBody TaskDTO taskDTO,
                                        @PathVariable TaskFields field) {

        try {
            return ResponseEntity.ok(taskService.changeTask(taskDTO, field));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка редактирования задачи.");
        }
    }

    @GetMapping()
    public ResponseEntity<?> allTasks(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(taskService.showAllTasks(userId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка поиска задач по id польователя.");
        }
    }

    @GetMapping("/{status}")
    public ResponseEntity<?> filterTasksByStatus(@PathVariable Long userId,
                                                 @PathVariable TaskStatus status) {
        try {
            return ResponseEntity.ok(taskService.filterTasksByStatus(userId, status));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка фильтрации задач по статусу.");
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


