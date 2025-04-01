package ru.zolotarev.todo.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.zolotarev.todo.dto.TaskDTO;
import ru.zolotarev.todo.entities.TaskEntity;
import ru.zolotarev.todo.entities.UserEntity;
import ru.zolotarev.todo.exceptions.user.UserNotFoundException;
import ru.zolotarev.todo.mappers.TaskListMapper;
import ru.zolotarev.todo.mappers.TaskMapper;
import ru.zolotarev.todo.repositories.TaskRepository;
import ru.zolotarev.todo.repositories.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @InjectMocks
    TaskService taskService;

    private static final Long USER_ID = 1L;
    private static final String USERNAME = "User";
    private static final String PASSWORD = "Password";
    private static final String EMAIL = "test@gmail.com";

    private static final Long TASK_ID = 3L;
    private static final String TITLE = "Title";
    private static final String DESCRIPTION = "description";

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private TaskMapper taskMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TaskListMapper taskListMapper;

    private UserEntity userEntity;
    private TaskEntity taskEntitySetUp;
    private TaskDTO taskDTOSetUp;

    @BeforeEach
    void setUp() {
        userEntity = new UserEntity();
        userEntity.setId(USER_ID);
        userEntity.setUsername(USERNAME);
        userEntity.setPassword(PASSWORD);
        userEntity.setEmail(EMAIL);

        taskEntitySetUp = new TaskEntity();
        taskEntitySetUp.setTitle(TITLE);
        taskEntitySetUp.setUserEntity(userEntity);
        taskEntitySetUp.setId(TASK_ID);
        taskEntitySetUp.setDescription(DESCRIPTION);

        taskDTOSetUp = new TaskDTO();
        taskDTOSetUp.setTitle(TITLE);
        taskDTOSetUp.setId(TASK_ID);
        taskDTOSetUp.setDescription(DESCRIPTION);
    }

    @Test
    void createTaskTest_Successfully() throws UserNotFoundException {

        when(userRepository.findById(USER_ID)).thenReturn(Optional.ofNullable(userEntity));
        when(taskRepository.save(taskEntitySetUp)).thenReturn(taskEntitySetUp);
        when(taskMapper.toDTO(taskEntitySetUp)).thenReturn(taskDTOSetUp);

        TaskDTO createdTask = taskService.createTask(taskEntitySetUp, USER_ID);

        assertEquals(taskDTOSetUp, createdTask);

        verify(userRepository, times(1)).findById(USER_ID);
        verify(taskRepository, times(1)).save(taskEntitySetUp);
        verify(taskMapper, times(1)).toDTO(taskEntitySetUp);

    }

    @Test
    void createTaskTest_ThrowUserNotFoundException() {

        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            taskService.createTask(taskEntitySetUp, USER_ID);
        });

        verify(userRepository, times(1)).findById(USER_ID);
    }
}
