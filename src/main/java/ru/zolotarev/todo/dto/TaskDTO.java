package ru.zolotarev.todo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.zolotarev.todo.enums.TaskStatus;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class TaskDTO {

    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDate deadline;

}
