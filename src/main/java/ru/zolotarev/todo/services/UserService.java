package ru.zolotarev.todo.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.zolotarev.todo.dto.UserDTO;
import ru.zolotarev.todo.entities.UserEntity;
import ru.zolotarev.todo.exceptions.task.UserAlreadyExistException;
import ru.zolotarev.todo.exceptions.user.UserNotFoundException;
import ru.zolotarev.todo.mappers.UserListMapper;
import ru.zolotarev.todo.mappers.UserMapper;
import ru.zolotarev.todo.repositories.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserListMapper userListMapper;

    public UserDTO createUser(UserEntity userEntity) throws UserAlreadyExistException {
        if (userRepository.existsByEmail(userEntity.getEmail())) {
            throw new UserAlreadyExistException();
        }
        return userMapper.toDTO(userRepository.save(userEntity));
    }

    public UserDTO getUserByEmail(String email) throws UserNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(email));
        return userMapper.toDTO(userEntity);
    }

    public String deleteUserById(Long id) throws UserNotFoundException {

        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return "Пользователь с id: '" + id + "' удален.";
        }
        throw new UserNotFoundException(id);
    }

    public List<UserDTO> getAllUsers() {
        List<UserEntity> userEntityList = (List<UserEntity>) userRepository.findAll();
        return userListMapper.toDTO(userEntityList);
    }

}
