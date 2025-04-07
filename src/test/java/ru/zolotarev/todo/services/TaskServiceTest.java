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
import ru.zolotarev.todo.enums.TaskStatus;
import ru.zolotarev.todo.exceptions.user.UserNotFoundException;
import ru.zolotarev.todo.mappers.TaskListMapper;
import ru.zolotarev.todo.mappers.TaskMapper;
import ru.zolotarev.todo.repositories.TaskRepository;
import ru.zolotarev.todo.repositories.UserRepository;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @InjectMocks
    TaskService taskService;

    private static final Long USER_ID = 1L;
    private static final String USERNAME = "User1";
    private static final String PASSWORD = "Password1";
    private static final String EMAIL = "user1@gmail.com";

    private static final Long FIRST_TASK_ID = 2L;
    private static final String FIRST_TITLE = "Title2";
    private static final String FIRST_DESCRIPTION = "description2";
    private static final String FIRST_STATUS = TaskStatus.TODO.toString();

    private static final Long SECOND_TASK_ID = 3L;
    private static final String SECOND_TITLE = "Title3";
    private static final String SECOND_DESCRIPTION = "description3";

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private TaskMapper taskMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TaskListMapper taskListMapper;

    private UserEntity userEntity;
    private TaskEntity taskEntityFirst;
    private TaskEntity taskEntitySecond;
    private TaskDTO taskDTOFirst;
    private TaskDTO taskDTOSecond;

    @BeforeEach
    void setUp() {
        userEntity = new UserEntity();
        userEntity.setId(USER_ID);
        userEntity.setUsername(USERNAME);
        userEntity.setPassword(PASSWORD);
        userEntity.setEmail(EMAIL);

        taskEntityFirst = new TaskEntity();
        taskEntityFirst.setTitle(FIRST_TITLE);
        taskEntityFirst.setUserEntity(userEntity);
        taskEntityFirst.setId(FIRST_TASK_ID);
        taskEntityFirst.setDescription(FIRST_DESCRIPTION);

        taskEntitySecond = new TaskEntity();
        taskEntitySecond.setTitle(SECOND_TITLE);
        taskEntitySecond.setUserEntity(userEntity);
        taskEntitySecond.setId(SECOND_TASK_ID);
        taskEntitySecond.setDescription(SECOND_DESCRIPTION);

        taskDTOFirst = new TaskDTO();
        taskDTOFirst.setTitle(FIRST_TITLE);
        taskDTOFirst.setId(FIRST_TASK_ID);
        taskDTOFirst.setDescription(FIRST_DESCRIPTION);

        taskDTOSecond = new TaskDTO();
        taskDTOSecond.setTitle(SECOND_TITLE);
        taskDTOSecond.setId(SECOND_TASK_ID);
        taskDTOSecond.setDescription(SECOND_DESCRIPTION);
    }

    @Test
    void createTaskTest_Successfully() throws Exception {

        when(userRepository.findById(USER_ID)).thenReturn(Optional.ofNullable(userEntity));
        when(taskRepository.save(taskEntityFirst)).thenReturn(taskEntityFirst);
        when(taskMapper.toDTO(taskEntityFirst)).thenReturn(taskDTOFirst);

        TaskDTO createdTask = taskService.createTask(taskDTOFirst, USER_ID);

        assertEquals(taskDTOFirst, createdTask);

        verify(userRepository, times(1)).findById(USER_ID);
        verify(taskRepository, times(1)).save(taskEntityFirst);
        verify(taskMapper, times(1)).toDTO(taskEntityFirst);

    }

    @Test
    void createTaskTest_ThrowUserNotFoundException() {

        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            taskService.createTask(taskDTOFirst, USER_ID);
        });

        verify(userRepository, times(1)).findById(USER_ID);
        verify(taskRepository, never()).save(taskEntityFirst);
        verify(taskMapper, never()).toDTO(taskEntityFirst);
    }

    static Stream<String> taskChangeFields() {
        return Stream.of("title", "description", "status", "deadline");
    }

//    @ParameterizedTest
////    @EnumSource(TaskStatus.class)
//    @MethodSource("taskChangeFields")
//    void changeTaskTest_Successfully() {
//
//        when(userRepository.findById(USER_ID)).thenReturn(Optional.ofNullable(userEntity));
//        when(userEntity.getTasks())
//
//    }
}
