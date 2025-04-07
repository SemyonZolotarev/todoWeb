package ru.zolotarev.todo.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
    public class UserDTO {

        private Long id;
        private String username;
        private String email;
        private List<TaskDTO> tasks;

    }
