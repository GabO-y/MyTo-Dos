package rz.springboot.mytodos.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
import rz.springboot.mytodos.model.Card;
import rz.springboot.mytodos.model.Task;
import rz.springboot.mytodos.model.User;
import rz.springboot.mytodos.model.dto.CardDto;
import rz.springboot.mytodos.model.dto.TaskDto;
import rz.springboot.mytodos.model.enums.Status;
import rz.springboot.mytodos.model.request.TaskRequest;
import rz.springboot.mytodos.repository.CardRepository;
import rz.springboot.mytodos.repository.TaskRepository;
import rz.springboot.mytodos.repository.UserRepository;

import java.net.URI;
import java.time.LocalDateTime;

//lida a lógica de negócio relacionada às tarefas (Task)
//interage com o repositório de tarefas (TaskRepository) para realizar operações de CRUD]
//é anotada com @Service para ser reconhecida como um componente de serviço pelo Spring
@Service
public class TaskService {

    //atributos finais para os repositórios necessários
    private final TaskRepository taskRepository;
    private final CardRepository cardRepository;
    private final UserRepository userRepository;


    //injeção de dependência via construtor
    public TaskService(TaskRepository taskRepository, CardRepository cardRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
    }

    //método auxiliar para obter o usuário atualmente autenticado
    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND ,"User with: " + username + " not found"));
    }

    //método para criar uma nova tarefa
    public ResponseEntity<TaskDto> postTask(TaskRequest request){

        //converte a requisição em uma entidade Task, salva no repositório e cria o DTO de resposta 
        TaskDto responseDto = new TaskDto(
            taskRepository.save(toEntity(request))
        );

        //cria a URI de localização da nova tarefa criada
        URI location = URI.create("tasks/" + responseDto.id());

        //retorna a resposta com o status 201 Created e o corpo contendo o DTO da tarefa criada
        return ResponseEntity.created(location).body(responseDto);

    }

    //método para buscar uma tarefa pelo ID
    public ResponseEntity<TaskDto> findById(Long id){

        //busca a tarefa no repositório pelo ID, lança exceção se não encontrada
        Task task = taskRepository.findById(id).orElseThrow(
            () ->  new ResponseStatusException(HttpStatus.NOT_FOUND)
        );

        //retorna a resposta com o status 200 OK e o corpo contendo o DTO da tarefa encontrada
        return ResponseEntity.ok(new TaskDto(task));
    }

    //método para editar uma tarefa 
    public ResponseEntity<TaskDto> editTask(TaskDto dto){

        //buscar uma tarefa existente pelo ID, lança exceção se não encontrada
        Task task = taskRepository.findById(dto.id()).orElseThrow(
            () -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Task with id: '" + dto.id() + "' not found"
            )
        );

        //atualiza os campos da tarefa 
        //com os valores do DTO, se não forem nulos
        if(dto.name() != null){
            task.setName(dto.name());
        }
        if(dto.description() != null){
            task.setDescription(dto.description());
        }
        if(dto.createdAt() != null){
            task.setCreatedAt(dto.createdAt());
        }

        //por ser uma data, precisa de uma validação
        //não pode ser nula nem antes da data de criação
        if(dto.expectedEnd() != null){
            if(task.getCreatedAt() != null && dto.expectedEnd().isBefore(task.getCreatedAt())){
                throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Expected end cannot be before creation time"
                );
            }
            task.setExpectedEnd(dto.expectedEnd());
        }

        //verifica se o cardName não é nulo
        //se não for, busca o card pelo nome e do usuário atual
        if(dto.cardName() != null){
            Card card = cardRepository.findByNameAndOwnerUsername(dto.cardName(), getCurrentUser().getUsername())
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Card not found with name: " + dto.cardName()
                ));
            task.setCard(card);
        }

        if(dto.status() != null){
            //O status deve ser um dos valores do enum Status
            task.setStatus(Status.valueOf(dto.status()));
        }

        task = taskRepository.save(task);

        return ResponseEntity.ok(new TaskDto(task));
    }

    public ResponseEntity<TaskDto> deleteTaskById(Long id){

        //busca a tarefa pelo ID, lança exceção se não encontrada
        Task task = taskRepository.findById(id).orElseThrow(
            () -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Task with id '" + id + "' not found"
            )
        );


        //em vez de apenas deletar a tarefa,
        //remove a tarefa da lista de tarefas do card associado
        //e salva o card atualizado no repositório
        Card card = task.getCard();
        card.getTasks().remove(task);
        cardRepository.save(card);
        taskRepository.delete(task);

        //retorna a resposta com o status 200 OK e o 
        //corpo contendo o DTO da tarefa 
        //que foi copiada antes de ser deletada
        return ResponseEntity.ok(new TaskDto(task));
    }


    //método auxiliar para converter um TaskRequest em uma entidade Task
    private Task toEntity(TaskRequest request){


        //entidade vazia que será populada e retornada
        Task entity = new Task();

        //popula os campos da entidade com os dados da requisição
        entity.setName(request.name());
        entity.setDescription(request.description());
        entity.setCreatedAt(LocalDateTime.now());

        //validação para expectedEnd
        //não pode ser antes da data atual
        if(request.expectedEnd() != null && request.expectedEnd().isBefore(entity.getCreatedAt())){
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Your expected time to end task could not be before now"
            );
        }
        entity.setExpectedEnd(request.expectedEnd());

        //busca o card pelo nome e do usuário atualmente autenticado
        Card card = cardRepository.findByNameAndOwnerUsername(request.cardName(), getCurrentUser().getUsername()).orElseThrow(
            () -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Card with name: '" + request.cardName() + "' not found"
            )
        );

        entity.setCard(card);
        //define o status inicial como DOING
        entity.setStatus(Status.valueOf("DOING"));

        return entity;

    }

    //método para buscar todas as tarefas com paginação
    //que vem como padrao do spring (se voce passar paginação)
    //se você não passar nada, ele pega a primeira página (0) com 20 itens
    public ResponseEntity<Page<TaskDto>> findAll(Pageable pageable) {

        //busca todas as tarefas com paginação e mapeia para DTOs
        //já que são os dtos que são expostos na API
        Page<TaskDto> task = taskRepository.findAll(pageable).map(TaskDto::new);

        return ResponseEntity.ok(task);

    }
}
