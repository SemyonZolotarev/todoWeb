package ru.zolotarev.todo.mappers;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.zolotarev.todo.dto.TaskDTO;
import ru.zolotarev.todo.entities.TaskEntity;

//@Mapper(componentModel = "spring", uses = UserMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TaskMapper {

    TaskDTO toDTO(TaskEntity taskEntity);

    @Mapping(target = "userEntity", ignore = true)
    TaskEntity toEntity(TaskDTO taskDTO);

//    @Mapping(source = "userEntity", target = "userId")
//    TaskDTO toDTO(TaskEntity taskEntity);
//
//    @Mapping(source = "userId", target = "userEntity")
//    TaskEntity toEntity(TaskDTO taskDTO, @Context UserRepository userRepository);
//
//    default UserEntity mapUserIdToUserEntity(Long userId, @Context UserRepository userRepository) {
//        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException());
//    }
//
//    default Long mapUserEntityToUserId(UserEntity userEntity){
//        return userEntity.getId();
//    }
}
