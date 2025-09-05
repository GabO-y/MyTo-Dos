package rz.springboot.mytodos.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rz.springboot.mytodos.model.dto.TaskDto;
import rz.springboot.mytodos.model.request.TaskRequest;
import rz.springboot.mytodos.service.TaskService;

//controlador REST para gerenciar tarefas (Task)
//é anotada com @RestController para ser reconhecida como um controlador REST pelo Spring
//e com @RequestMapping("/tasks") para mapear as requisições HTTP para o
@RestController()
@RequestMapping("/tasks")
public class TaskController {

    //atributo para o serviço das tasks
    private final TaskService taskService;

    //injeção de dependência via construtor
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    //endpoints para operações CRUD de tarefas
    @GetMapping()
    public ResponseEntity<Page<TaskDto>> findAll(Pageable pageable){
        return taskService.findAll(pageable);
    }

    //endpoint para buscar uma tarefa pelo id
    //variable path é para capturar o id da tarefa na URL
    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> findById(@PathVariable Long id){
        return taskService.findById(id);
    }

    //endpoint para criar uma nova tarefa
    //o request body é para capturar os dados da tarefa no corpo da requisição
    @PostMapping()
    public ResponseEntity<TaskDto> postTask(@RequestBody TaskRequest task){
        return taskService.postTask(task);
    }

    //endpoint para editar uma tarefa existente
    @PutMapping
    public ResponseEntity<TaskDto> editTask(@RequestBody TaskDto dto){
        return taskService.editTask(dto);
    }

    //endpoint para deletar uma tarefa pelo id
    @DeleteMapping("/{id}")
    public ResponseEntity<TaskDto> deleteTask(@PathVariable Long id){
        return taskService.deleteTaskById(id);
    }

}
