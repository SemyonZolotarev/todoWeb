package ru.zolotarev.todo.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.zolotarev.todo.entities.TaskEntity;

import java.util.List;

@Repository
public interface TaskRepository extends CrudRepository<TaskEntity, Long> {
    List<TaskEntity> findByUserEntity_Id(Long userId);
}
