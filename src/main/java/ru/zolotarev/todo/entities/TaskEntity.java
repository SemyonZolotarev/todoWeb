package ru.zolotarev.todo.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.zolotarev.todo.enums.TaskStatus;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @Column
    private String taskStatus = String.valueOf(TaskStatus.TODO);

    @Column
    private LocalDate deadline = LocalDate.now().plusDays(1);

}
