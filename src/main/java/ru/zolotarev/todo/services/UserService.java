package ru.zolotarev.todo.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zolotarev.todo.dto.UserDTO;
import ru.zolotarev.todo.entities.UserEntity;
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

    @Transactional
    public UserDTO createUser(UserEntity userEntity) {
        return userMapper.toDTO(userRepository.save(userEntity));
    }

    @Transactional
    public UserDTO getUserByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email).get();
        return userMapper.toDTO(userEntity);
    }

    @Transactional
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public List<UserDTO> getAllUsers() {
        return userListMapper.toDTO(userRepository.findAll());
    }

}
