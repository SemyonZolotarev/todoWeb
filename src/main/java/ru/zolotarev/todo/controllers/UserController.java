package ru.zolotarev.todo.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.zolotarev.todo.dto.UserDTO;
import ru.zolotarev.todo.entities.UserEntity;
import ru.zolotarev.todo.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1.3/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserEntity userEntity) {
        try {
            return ResponseEntity.ok(userService.createUser(userEntity));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка создания нового пользователя.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUserById(id);
            return ResponseEntity.ok("Пользователь успешно удален");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка удаления пользователя.");
        }
    }

    @GetMapping("/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        try {
            return ResponseEntity.ok(userService.getUserByEmail(email));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка получения пользователя по email.");
        }
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> allUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

}
