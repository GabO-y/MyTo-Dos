package rz.springboot.mytodos.model.dto;

import rz.springboot.mytodos.model.Card;
import rz.springboot.mytodos.model.Task;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

//record é uma classe imutável que encapsula dados
//essa é usada para transferir dados de um card
public record CardDto(Long id, String name, String description, Long ownerId, Set<TaskDto> tasks) {

    //construtor que mapeia uma entidade Card para CardDto
    public CardDto(Card card){
        this(card.getId(), card.getName(), card.getDescription(), card.getOwner().getId(),
        //como as tarefas de um card é uma coleção, mapeia cada tarefa para TaskDto
            card.getTasks() != null ?
                card.getTasks()
                    .stream()
                    //um dos construtores de TaskDto que recebe uma entidade Task
                    //por isso é usado o method reference
                    .map(TaskDto::new)
                    .collect(Collectors.toSet())
                //se a coleção de tarefas for nula, inicializa com um conjunto vazio
                : new LinkedHashSet<>()
        );
    }
   
}



