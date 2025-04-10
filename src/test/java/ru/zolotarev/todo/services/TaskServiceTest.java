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
import ru.zolotarev.todo.repositories.UserRepository;

import java.time.LocalDate;
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
    private UserRepository userRepository;
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
    void createTaskTest_Successfully() throws Exception {

        Optional<UserEntity> optionalUserEntity = Optional.of(userEntity);

        when(userRepository.existsById(USER_ID)).thenReturn(true);
        when(userRepository.findById(USER_ID)).thenReturn(optionalUserEntity);
        when(taskMapper.toEntity(taskDTO)).thenReturn(taskEntity);
        when(taskRepository.save(taskEntity)).thenReturn(taskEntity);
        when(taskMapper.toDTO(taskEntity)).thenReturn(taskDTO);

        TaskDTO createdTask = taskService.createTask(taskDTO, USER_ID);

        assertEquals(taskDTO, createdTask);

        verify(userRepository, times(1)).findById(USER_ID);
        verify(taskRepository, times(1)).save(taskEntity);
        verify(taskMapper, times(1)).toEntity(taskDTO);
        verify(taskMapper, times(1)).toDTO(taskEntity);

    }

//    @Test
//    void createTaskTest_ThrowUserNotFoundException() {
//
//        when(userRepository.existsById(USER_ID)).thenReturn(false);
//
//        assertThrows(UserNotFoundException.class, () -> {
//            taskService.createTask(taskDTO, USER_ID);
//        });
//
//        verify(taskRepository, never()).save(taskEntity);
//    }

    @ParameterizedTest
    @EnumSource(TaskFields.class)
    void changeTaskTest_Successfully(TaskFields field) throws Exception {

        when(taskRepository.findById(taskDTO.getId()).get()).thenReturn(taskEntity);
        when(taskMapper.toDTO(taskEntity)).thenReturn(taskDTO);

        TaskDTO changedTask = taskService.changeTask(taskDTO, field);

        switch (field) {
            case TITLE -> assertEquals(taskDTO.getTitle(), changedTask.getTitle());
            case DESCRIPTION -> assertEquals(taskDTO.getDescription(), changedTask.getDescription());
            case STATUS -> assertEquals(taskDTO.getStatus(), changedTask.getStatus());
            case DEADLINE -> assertEquals(taskDTO.getDeadline(), changedTask.getDeadline());
            default -> fail("Неизвестное поле");
        }

        verify(taskRepository, times(1)).findByUserEntity_Id(anyLong());
        verify(taskRepository, times(1)).save(taskEntity);
        verify(taskMapper, times(1)).toDTO(taskEntity);

    }

//    @Test
//    void changeTaskTest_incorrectUserId_throwsException() throws Exception {
//
//        when(taskRepository.findByUserEntity_Id(anyLong())).thenReturn(Collections.emptyList());
//
//        assertThrows(Exception.class, () -> {
//            taskService.changeTask(taskDTO, anyLong(), TaskFields.DEADLINE);
//        });
//
//        verify(taskRepository, never()).save(any());
//    }

}
