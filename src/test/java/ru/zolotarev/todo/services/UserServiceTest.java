package ru.zolotarev.todo.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.zolotarev.todo.dto.UserDTO;
import ru.zolotarev.todo.entities.UserEntity;
import ru.zolotarev.todo.exceptions.user.UserNotFoundException;
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

    private UserEntity userEntitySetUp;
    private UserDTO userDTOSetUp;

    @BeforeEach
    void setUp() {
        userEntitySetUp = new UserEntity();
        userEntitySetUp.setUsername(USERNAME);
        userEntitySetUp.setEmail(EMAIL);

        userDTOSetUp = new UserDTO();
        userDTOSetUp.setUsername(USERNAME);
        userDTOSetUp.setEmail(EMAIL);
    }

    @Test
    void createUserTest_UserDoesNotExist_CreateSuccessfully() throws Exception {

        when(userRepository.existsByEmail(userEntitySetUp.getEmail())).thenReturn(false);
        when(userRepository.save(userEntitySetUp)).thenReturn(userEntitySetUp);
        when(userMapper.toDTO(userEntitySetUp)).thenReturn(userDTOSetUp);

        UserDTO createdUser = userService.createUser(userEntitySetUp);

        assertNotNull(createdUser);
        assertEquals(userEntitySetUp.getEmail(), createdUser.getEmail());

        verify(userRepository, times(1)).save(userEntitySetUp);
    }

    @Test
    void createUserTest_UserExist_ThrowException() {

        when(userRepository.existsByEmail(userEntitySetUp.getEmail())).thenReturn(true);

        assertThrows(Exception.class, () -> {
            userService.createUser(userEntitySetUp);
        });

        verify(userRepository, times(1)).existsByEmail(userEntitySetUp.getEmail());
        verify(userRepository, never()).save(userEntitySetUp);
    }


    @Test
    void getUserByEmailTest_Successfully() throws UserNotFoundException {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.ofNullable(userEntitySetUp));
        when(userMapper.toDTO(userEntitySetUp)).thenReturn(userDTOSetUp);

        UserDTO userByEmail = userService.getUserByEmail(EMAIL);

        assertEquals(userDTOSetUp, userByEmail);

        verify(userRepository, times(1)).findByEmail(EMAIL);
        verify(userMapper, times(1)).toDTO(userEntitySetUp);
    }

//    @Test
//    void getUserByEmailTest_ThrowsUserNotFoundException() {
//        when(userRepository.findByEmail(EMAIL)).thenThrow(new UserNotFoundException(EMAIL));
//
//        assertThrows(UserNotFoundException.class, () -> {
//            userService.getUserByEmail(EMAIL);
//        });
//
//        verify(userRepository, times(1)).findByEmail(EMAIL);
//    }

//    @Test
//    void deleteUserByIdTest_Successfully() throws UserNotFoundException {
//
//        when(userRepository.existsById(ID)).thenReturn(true);
//
//        String result = userService.deleteUserById(ID);
//
//        assertEquals("Пользователь с id: '" + ID + "' удален.", result);
//
//        verify(userRepository, times(1)).deleteById(ID);
//    }

    @Test
    void deleteUserByIdTest_ThrowUserNotFoundException() {

        when(userRepository.existsById(ID)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> {
            userService.deleteUserById(ID);
        });

        verify(userRepository, never()).deleteById(ID);
    }

    @Test
    void getAllUsersTest() {

        UserEntity userEntity = new UserEntity();
        UserDTO userDTO = new UserDTO();
        List<UserEntity> userEntityList = List.of(userEntitySetUp, userEntity);

        when(userRepository.findAll()).thenReturn(userEntityList);
        when(userListMapper.toDTO(userEntityList)).thenReturn(List.of(userDTOSetUp, userDTO));

        assertEquals(userService.getAllUsers(), List.of(userDTOSetUp, userDTO));

        verify(userRepository, times(1)).findAll();
        verify(userListMapper, times(1)).toDTO(userEntityList);

    }
}
