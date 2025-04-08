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

    private static final Long TASK_ID = 1L;

    private static final Long USER_ID = 1L;
    private static final String USER_EMAIL = "user@gmail.com";
    private TaskDTO taskDTO;

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TaskMapper taskMapper;
    @Mock
    private UserEntity userEntity;
    @Mock
    private TaskEntity taskEntity;


    @BeforeEach
    void setUp() {
        taskDTO = new TaskDTO();
        taskDTO.setId(TASK_ID);
    }

    @Test
    void createTaskTest_Successfully() throws Exception {

        when(userRepository.existsById(USER_ID)).thenReturn(true);
        when(userRepository.findById(USER_ID).get()).thenReturn(userEntity);
        when(taskMapper.toDTO(taskRepository.save(taskEntity))).thenReturn(taskDTO);
        when(taskMapper.toEntity(taskDTO)).thenReturn(taskEntity);

        TaskDTO createdTask = taskService.createTask(taskDTO, USER_ID);

        assertEquals(taskDTO, createdTask);

        verify(userRepository, times(1)).findById(USER_ID);
        verify(taskRepository, times(1)).save(taskEntity);
        verify(taskMapper, times(1)).toDTO(taskEntity);

    }

//    @Test
//    void createTaskTest_ThrowUserNotFoundException() {
//
//        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());
//
//        assertThrows(UserNotFoundException.class, () -> {
//            taskService.createTask(taskDTO, USER_ID);
//        });
//
//        verify(userRepository, times(1)).findById(USER_ID);
//        verify(taskRepository, never()).save(taskEntity);
//        verify(taskMapper, never()).toDTO(taskEntity);
//    }

    @Test
    void changeTaskTest_Successfully() {

    }
}
