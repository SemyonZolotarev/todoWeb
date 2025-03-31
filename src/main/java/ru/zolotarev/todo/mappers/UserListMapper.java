package ru.zolotarev.todo.mappers;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import ru.zolotarev.todo.dto.UserDTO;
import ru.zolotarev.todo.entities.UserEntity;

import java.util.List;

@Mapper(componentModel = "spring", uses = UserMapper.class,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserListMapper {

    List<UserDTO> toDTO(List<UserEntity> list);
}
