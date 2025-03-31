package ru.zolotarev.todo.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.zolotarev.todo.dto.TaskDTO;
import ru.zolotarev.todo.entities.TaskEntity;
import ru.zolotarev.todo.entities.UserEntity;
import ru.zolotarev.todo.enums.SortTaskByDeadlineMethods;
import ru.zolotarev.todo.enums.TaskFields;
import ru.zolotarev.todo.exceptions.task.TaskNotFoundException;
import ru.zolotarev.todo.exceptions.user.UserNotFoundException;
import ru.zolotarev.todo.mappers.TaskListMapper;
import ru.zolotarev.todo.mappers.TaskMapper;
import ru.zolotarev.todo.repositories.TaskRepository;
import ru.zolotarev.todo.repositories.UserRepository;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    private final TaskMapper taskMapper;
    private final UserRepository userRepository;
    private final TaskListMapper taskListMapper;

    private UserEntity findByUserId(Long userId) throws UserNotFoundException {
        return userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException(userId));
    }

    public TaskDTO createTask(TaskEntity taskEntity, Long userId) throws UserNotFoundException {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException(userId));
        taskEntity.setUserEntity(userEntity);
        return taskMapper.toDTO(taskRepository.save(taskEntity));
    }

    public TaskDTO changeTask(TaskDTO taskDTO, Long userId, String field)
            throws UserNotFoundException, TaskNotFoundException, IllegalStateException {

        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException(userId));

        TaskEntity taskEntity = userEntity.getTasks()
                .stream()
                .filter(t -> t.getId() == taskDTO.getId())
                .findFirst().orElseThrow(() ->
                        new TaskNotFoundException(taskDTO.getId()));

        switch (TaskFields.valueOf(field.toUpperCase())) {
            case TITLE -> taskEntity.setTitle(taskDTO.getTitle());
            case DESCRIPTION -> taskEntity.setDescription(taskDTO.getDescription());
            case STATUS -> taskEntity.setTaskStatus(taskDTO.getTaskStatus().toUpperCase());
            case DEADLINE -> taskEntity.setDeadline(taskDTO.getDeadline());
        }

        return taskMapper.toDTO(taskRepository.save(taskEntity));
    }


    public List<TaskDTO> showAllTasks(Long userId) {
        List<TaskEntity> tasks = taskRepository.findByUserEntity_Id(userId);
        return taskListMapper.toDTO(tasks);
    }

    public List<TaskDTO> filterTasksByStatus(Long userId, String taskStatus) throws UserNotFoundException {

        List<TaskEntity> filteredTasks = findByUserId(userId).getTasks()
                .stream()
                .filter(t -> t.getTaskStatus().equalsIgnoreCase(taskStatus))
                .collect(Collectors.toList());

        return taskListMapper.toDTO(filteredTasks);
    }

    public List<TaskDTO> sortTasksByStatus(Long userId, String first,
                                           String second, String third) throws UserNotFoundException {

        Map<String, Integer> statusOrder = new HashMap<>();
        statusOrder.put(first.toUpperCase(), 1);
        statusOrder.put(second.toUpperCase(), 2);
        statusOrder.put(third.toUpperCase(), 3);

        List<TaskEntity> sortedTasks = findByUserId(userId).getTasks()
                .stream()
                .sorted(Comparator.comparingInt(t -> statusOrder.get(t.getTaskStatus())))
                .collect(Collectors.toList());
        return taskListMapper.toDTO(sortedTasks);
    }

    public List<TaskDTO> sortTaskByDeadline(Long userId, String method) throws UserNotFoundException, IllegalStateException {

        findByUserId(userId);

        List<TaskEntity> sortedTasks = findByUserId(userId).getTasks()
                .stream()
                .sorted(Comparator.comparing(task -> task.getDeadline()))
                .collect(Collectors.toList());

        switch (SortTaskByDeadlineMethods.valueOf(method.toUpperCase())) {
            case NATURAL -> {
                return taskListMapper.toDTO(sortedTasks);
            }
            case REVERSED -> {
                return taskListMapper.toDTO(sortedTasks).reversed();
            }
            default -> throw new IllegalStateException("Unexpected value: " + method);
        }
    }

}
