package ru.zolotarev.todo.mappers;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import ru.zolotarev.todo.dto.TaskDTO;
import ru.zolotarev.todo.entities.TaskEntity;

import java.util.List;

@Mapper(componentModel = "spring", uses = TaskMapper.class,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TaskListMapper {

    List<TaskDTO> toDTO(List<TaskEntity> list);
}
