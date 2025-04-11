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
    void getUserByEmailTest_ThrowsUserNotFoundException() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            userService.getUserByEmail(EMAIL);
        });

        verify(userRepository, times(1)).findByEmail(EMAIL);
    }

//    @Test
//    void deleteUserByIdTest_Successfully(){
//
//        when(userRepository.existsById(ID)).thenReturn(true);
//
//        String result = userService.deleteUserById(ID);
//
//        assertEquals("Пользователь с id: '" + ID + "' удален.", result);
//
//        verify(userRepository, times(1)).deleteById(ID);
//    }

//    @Test
//    void deleteUserByIdTest_ThrowUserNotFoundException() {
//
//        when(userRepository.existsById(ID)).thenReturn(false);
//
//        assertThrows(UserNotFoundException.class, () -> {
//            userService.deleteUserById(ID);
//        });
//
//        verify(userRepository, never()).deleteById(ID);
//    }

    @Test
    void getAllUsersTest() {

        UserEntity userEntity = new UserEntity();
        UserDTO userDTO = new UserDTO();
        List<UserEntity> userEntityList = List.of(this.userEntity, userEntity);

        when(userRepository.findAll()).thenReturn(userEntityList);
        when(userListMapper.toDTO(userEntityList)).thenReturn(List.of(this.userDTO, userDTO));

        assertEquals(userService.getAllUsers(), List.of(this.userDTO, userDTO));

        verify(userRepository, times(1)).findAll();
        verify(userListMapper, times(1)).toDTO(userEntityList);

    }
}
