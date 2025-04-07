package ru.zolotarev.todo.mappers;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import ru.zolotarev.todo.dto.UserDTO;
import ru.zolotarev.todo.entities.UserEntity;

@Mapper(componentModel = "spring", uses = TaskListMapper.class,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {

    UserDTO toDTO(UserEntity userEntity);
}
