package ru.zolotarev.todo.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.zolotarev.todo.dto.TaskDTO;
import ru.zolotarev.todo.entities.TaskEntity;
import ru.zolotarev.todo.entities.UserEntity;
import ru.zolotarev.todo.enums.TaskFields;
import ru.zolotarev.todo.enums.TaskStatus;
import ru.zolotarev.todo.mappers.TaskMapper;
import ru.zolotarev.todo.repositories.TaskRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @InjectMocks
    TaskService taskService;

    private static final Long TASK_ID = 1L;
    private static final String TASK_TITLE = "TASK_TITLE";
    private static final String TASK_DESCRIPTION = "TASK_DESCRIPTION";
    private static final TaskStatus TASK_STATUS = TaskStatus.TODO;
    private static final LocalDate TASK_DEADLINE = LocalDate.now();

    private static final Long USER_ID = 1L;
    private static final String USER_EMAIL = "user@gmail.com";
    private static final String USER_USERNAME = "Username";
    private static final String USER_PASSWORD = "Password";

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private TaskMapper taskMapper;


    private UserEntity userEntity;
    private TaskEntity taskEntity;
    private TaskDTO taskDTO;

    @BeforeEach
    void setUp() {
        taskDTO = new TaskDTO();
        taskDTO.setId(TASK_ID);
        taskDTO.setTitle(TASK_TITLE);
        taskDTO.setDescription(TASK_DESCRIPTION);
        taskDTO.setStatus(TASK_STATUS);
        taskDTO.setDeadline(TASK_DEADLINE);

        taskEntity = new TaskEntity();
        taskEntity.setId(TASK_ID);
        taskEntity.setTitle(TASK_TITLE);
        taskEntity.setDescription(TASK_DESCRIPTION);
        taskEntity.setUserEntity(userEntity);
        taskEntity.setStatus(TASK_STATUS);
        taskEntity.setDeadline(TASK_DEADLINE);

        userEntity = new UserEntity();
        userEntity.setId(USER_ID);
        userEntity.setUsername(USER_USERNAME);
        userEntity.setPassword(USER_PASSWORD);
        userEntity.setEmail(USER_EMAIL);
    }

    @Test
    void createTaskTest_Successfully() {

        List<TaskEntity> taskEntityList = List.of(taskEntity);

        when(taskRepository.findByUserEntity_Id(USER_ID)).thenReturn(taskEntityList);
        when(taskMapper.toEntity(taskDTO)).thenReturn(taskEntity);
        when(taskRepository.save(taskEntity)).thenReturn(taskEntity);
        when(taskMapper.toDTO(taskEntity)).thenReturn(taskDTO);

        TaskDTO createdTask = taskService.createTask(taskDTO, USER_ID);

        assertEquals(taskDTO, createdTask);

        verify(taskMapper, times(1)).toEntity(taskDTO);
        verify(taskRepository, times(1)).save(taskEntity);
        verify(taskMapper, times(1)).toDTO(taskEntity);

    }

    @ParameterizedTest
    @EnumSource(TaskFields.class)
    void changeTaskTest_Successfully(TaskFields field) {

        Optional<TaskEntity> taskEntityOptional = Optional.of(taskEntity);

        when(taskRepository.findById(taskDTO.getId())).thenReturn(taskEntityOptional);
        when(taskRepository.save(taskEntity)).thenReturn(taskEntity);
        when(taskMapper.toDTO(taskEntity)).thenReturn(taskDTO);

        TaskDTO changedTask = taskService.changeTask(taskDTO, field);

        switch (field) {
            case TITLE -> assertEquals(taskDTO.getTitle(), changedTask.getTitle());
            case DESCRIPTION -> assertEquals(taskDTO.getDescription(), changedTask.getDescription());
            case STATUS -> assertEquals(taskDTO.getStatus(), changedTask.getStatus());
            case DEADLINE -> assertEquals(taskDTO.getDeadline(), changedTask.getDeadline());
            default -> fail("Неизвестное поле");
        }

        verify(taskRepository, times(1)).findById(anyLong());
        verify(taskRepository, times(1)).save(taskEntity);
        verify(taskMapper, times(1)).toDTO(taskEntity);

    }

}
