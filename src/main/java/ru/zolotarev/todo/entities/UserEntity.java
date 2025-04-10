package ru.zolotarev.todo.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String username;

    @Column
    private String password;

    @Column(unique = true)
    private String email;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userEntity")
    private List<TaskEntity> tasks = new ArrayList<>();
}
