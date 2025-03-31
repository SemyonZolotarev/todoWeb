package ru.zolotarev.todo.mappers;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import ru.zolotarev.todo.dto.TaskDTO;
import ru.zolotarev.todo.entities.TaskEntity;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TaskMapper {

    TaskDTO toDTO(TaskEntity taskEntity);
}
