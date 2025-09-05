package rz.springboot.mytodos.model.dto;

import rz.springboot.mytodos.model.User;

import java.util.Set;

public record UserDto(String login, Set<TaskDto> idsTasks) {

    //construtor que mapeia uma entidade User para UserDto
    public UserDto(User entity){
        this(entity.getUsername(), null);
    }

}
