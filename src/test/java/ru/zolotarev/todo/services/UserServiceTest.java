package ru.zolotarev.todo.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import ru.zolotarev.todo.dto.UserDTO;
import ru.zolotarev.todo.entities.UserEntity;
import ru.zolotarev.todo.mappers.UserListMapper;
import ru.zolotarev.todo.mappers.UserMapper;
import ru.zolotarev.todo.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final String EMAIL = "test@gmail.com";
    private static final String USERNAME = "User";
    private static final Long ID = 1L;


    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private UserListMapper userListMapper;

    private UserEntity userEntity;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        userEntity = new UserEntity();
        userEntity.setUsername(USERNAME);
        userEntity.setEmail(EMAIL);

        userDTO = new UserDTO();
        userDTO.setUsername(USERNAME);
        userDTO.setEmail(EMAIL);
    }

    @Test
    void createUserTest_CreateSuccessfully() {

        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(userMapper.toDTO(userEntity)).thenReturn(userDTO);

        UserDTO createdUser = userService.createUser(userEntity);

        assertNotNull(createdUser);
        assertEquals(userEntity.getEmail(), createdUser.getEmail());

        verify(userRepository, times(1)).save(userEntity);
    }

    @Test
    void createUserTest_DuplicateEmail_ThrowsException() {

        when(userRepository.save(userEntity)).thenThrow(new DataIntegrityViolationException("Error"));

        assertThrows(DataIntegrityViolationException.class, () -> {
            userService.createUser(userEntity);
        });

        verifyNoInteractions(userMapper);
    }


    @Test
    void getUserByEmailTest_Successfully() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.ofNullable(userEntity));
        when(userMapper.toDTO(userEntity)).thenReturn(userDTO);

        UserDTO userByEmail = userService.getUserByEmail(EMAIL);

        assertEquals(userDTO, userByEmail);

        verify(userRepository, times(1)).findByEmail(EMAIL);
        verify(userMapper, times(1)).toDTO(userEntity);
    }

    @Test
    void getUserByEmailTest_invalidOrIncorrectEmail_ThrowsException() {

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            userService.getUserByEmail(EMAIL);
        });

        verify(userRepository, times(1)).findByEmail(EMAIL);
    }

    @Test
    void getAllUsersTest() {

        UserEntity newUserEntity = new UserEntity();
        UserDTO newUserDTO = new UserDTO();
        List<UserEntity> userEntityList = List.of(userEntity, newUserEntity);

        when(userRepository.findAll()).thenReturn(userEntityList);
        when(userListMapper.toDTO(userEntityList)).thenReturn(List.of(userDTO, newUserDTO));

        assertEquals(userService.getAllUsers(), List.of(userDTO, newUserDTO));

        verify(userRepository, times(1)).findAll();
        verify(userListMapper, times(1)).toDTO(userEntityList);

    }
}
