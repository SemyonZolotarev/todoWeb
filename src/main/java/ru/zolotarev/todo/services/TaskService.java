package ru.zolotarev.todo.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zolotarev.todo.dto.TaskDTO;
import ru.zolotarev.todo.entities.TaskEntity;
import ru.zolotarev.todo.entities.UserEntity;
import ru.zolotarev.todo.enums.SortByDeadlineMethods;
import ru.zolotarev.todo.enums.TaskFields;
import ru.zolotarev.todo.enums.TaskStatus;
import ru.zolotarev.todo.mappers.TaskListMapper;
import ru.zolotarev.todo.mappers.TaskMapper;
import ru.zolotarev.todo.repositories.TaskRepository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final TaskListMapper taskListMapper;

    @Transactional
    public TaskDTO createTask(TaskDTO taskDTO, Long userId) {
        UserEntity userEntity =
                taskRepository.findByUserEntity_Id(userId)
                        .getFirst()
                        .getUserEntity();
        TaskEntity taskEntity = taskMapper.toEntity(taskDTO);
        taskEntity.setUserEntity(userEntity);
        return taskMapper.toDTO(taskRepository.save(taskEntity));
    }


    @Transactional
    public TaskDTO changeTask(TaskDTO taskDTO, TaskFields field) {

        TaskEntity taskEntity = taskRepository.findById(taskDTO.getId()).get();

        switch (field) {
            case TITLE -> taskEntity.setTitle(taskDTO.getTitle());
            case DESCRIPTION -> taskEntity.setDescription(taskDTO.getDescription());
            case STATUS -> taskEntity.setStatus(taskDTO.getStatus());
            case DEADLINE -> taskEntity.setDeadline(taskDTO.getDeadline());
            default -> throw new RuntimeException();
        }

        return taskMapper.toDTO(taskRepository.save(taskEntity));
    }


    @Transactional
    public List<TaskDTO> showAllTasks(Long userId) {
        List<TaskEntity> tasks = taskRepository.findByUserEntity_Id(userId);
        return taskListMapper.toDTO(tasks);
    }

    @Transactional
    public List<TaskDTO> filterTasksByStatus(Long userId, TaskStatus taskStatus) {

        List<TaskEntity> filteredTasks =
                taskRepository.findByUserEntity_Id(userId)
                        .stream()
                        .filter(t -> Objects.equals(t.getStatus(), taskStatus))
                        .collect(Collectors.toList());

        return taskListMapper.toDTO(filteredTasks);
    }

    @Transactional
    public List<TaskDTO> sortTasksByDeadline(Long userId, SortByDeadlineMethods method) {

        List<TaskEntity> sortedTasks =
                taskRepository.findByUserEntity_Id(userId)
                        .stream()
                        .sorted(Comparator.comparing(task -> task.getDeadline()))
                        .collect(Collectors.toList());

        List<TaskDTO> tasks = taskListMapper.toDTO(sortedTasks);

        switch (method) {
            case NATURAL -> {
                return tasks;
            }
            case REVERSED -> {
                Collections.reverse(tasks);
                return tasks;
            }
            default -> throw new RuntimeException();
        }
    }

}
