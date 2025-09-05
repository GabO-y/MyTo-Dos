package rz.springboot.mytodos.model.dto;

import org.springframework.cglib.core.Local;
import rz.springboot.mytodos.model.Task;

import java.time.LocalDateTime;

//record é uma classe imutável que encapsula dados
//essa é usada para transferir dados de uma tarefa
public record TaskDto(Long id, String name, String description, LocalDateTime createdAt, LocalDateTime expectedEnd, String cardName, String status){

    //construtor que mapeia uma entidade Task para TaskDto
    public TaskDto(Task task){
        this(task.getId(), task.getName(), task.getDescription(),task.getCreatedAt(), task.getExpectedEnd(), task.getCard().getName(), task.getStatus().toString());
    }


}
