package ru.zolotarev.todo.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.zolotarev.todo.dto.TaskDTO;
import ru.zolotarev.todo.entities.TaskEntity;
import ru.zolotarev.todo.entities.UserEntity;
import ru.zolotarev.todo.enums.SortByDeadlineMethods;
import ru.zolotarev.todo.enums.TaskFields;
import ru.zolotarev.todo.enums.TaskStatus;
import ru.zolotarev.todo.exceptions.user.UserNotFoundException;
import ru.zolotarev.todo.mappers.TaskListMapper;
import ru.zolotarev.todo.mappers.TaskMapper;
import ru.zolotarev.todo.repositories.TaskRepository;
import ru.zolotarev.todo.repositories.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserRepository userRepository;
    private final TaskListMapper taskListMapper;

    private UserEntity findByUserId(Long userId) throws Exception {
        return userRepository.findById(userId).orElseThrow(() ->
                new Exception());
    }

    //    public TaskDTO createTask(TaskDTO taskDTO, Long userId) throws UserNotFoundException {
//        if (userRepository.existsById(userId)) {
//            UserEntity userEntity = userRepository.findById(userId).get();
//            TaskEntity taskEntity = taskMapper.toEntity(taskDTO);
//            taskEntity.setUserEntity(userEntity);
//            taskRepository.save(taskEntity);
//            return taskDTO;
//        }
//        throw new UserNotFoundException(userId);
//    }
    public TaskDTO createTask(TaskEntity taskEntity, Long userId) throws UserNotFoundException {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException(userId));
        taskEntity.setUserEntity(userEntity);
        return taskMapper.toDTO(taskRepository.save(taskEntity));
    }

    public TaskDTO changeTask(TaskDTO taskDTO, Long userId, TaskFields field)
            throws Exception {

        TaskEntity taskEntity = taskRepository.findByUserEntity_Id(userId)
                .stream()
                .filter(t -> Objects.equals(t.getId(), taskDTO.getId()))
                .findFirst().orElseThrow(() ->
                        new Exception());

        switch (field) {
            case TITLE -> taskEntity.setTitle(taskDTO.getTitle());
            case DESCRIPTION -> taskEntity.setDescription(taskDTO.getDescription());
            case STATUS -> taskEntity.setStatus(taskDTO.getStatus());
            case DEADLINE -> taskEntity.setDeadline(taskDTO.getDeadline());
            default -> throw new Exception();
        }

        return taskMapper.toDTO(taskRepository.save(taskEntity));
    }


    public List<TaskDTO> showAllTasks(Long userId) {
        List<TaskEntity> tasks = taskRepository.findByUserEntity_Id(userId);
        return taskListMapper.toDTO(tasks);
    }

    public List<TaskDTO> filterTasksByStatus(Long userId, TaskStatus taskStatus) throws Exception {

        List<TaskEntity> filteredTasks = findByUserId(userId).getTasks()
                .stream()
                .filter(t -> t.getStatus().equals(taskStatus))
                .collect(Collectors.toList());

        return taskListMapper.toDTO(filteredTasks);
    }

    public List<TaskDTO> sortTasksByStatus(Long userId, String first,
                                           String second, String third) throws Exception {

        Map<String, Integer> statusOrder = new HashMap<>();
        statusOrder.put(first.toUpperCase(), 1);
        statusOrder.put(second.toUpperCase(), 2);
        statusOrder.put(third.toUpperCase(), 3);

        List<TaskEntity> sortedTasks = findByUserId(userId).getTasks()
                .stream()
                .sorted(Comparator.comparingInt(t -> statusOrder.get(t.getStatus().name())))
                .collect(Collectors.toList());
        return taskListMapper.toDTO(sortedTasks);
    }

    public List<TaskDTO> sortTaskByDeadline(Long userId, SortByDeadlineMethods method) throws Exception {

        List<TaskEntity> sortedTasks = findByUserId(userId).getTasks()
                .stream()
                .sorted(Comparator.comparing(task -> task.getDeadline()))
                .collect(Collectors.toList());

        switch (method) {
            case NATURAL -> {
                return taskListMapper.toDTO(sortedTasks);
            }
            case REVERSED -> {
                return taskListMapper.toDTO(sortedTasks).reversed();
            }
            default -> throw new Exception();
        }
    }

}
