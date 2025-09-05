package rz.springboot.mytodos.service;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import rz.springboot.mytodos.model.Card;
import rz.springboot.mytodos.model.Task;
import rz.springboot.mytodos.model.User;
import rz.springboot.mytodos.model.dto.CardDto;
import rz.springboot.mytodos.model.dto.TaskDto;
import rz.springboot.mytodos.model.enums.Status;
import rz.springboot.mytodos.model.request.CardRequest;
import rz.springboot.mytodos.repository.CardRepository;
import rz.springboot.mytodos.repository.UserRepository;

import java.net.URI;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CardService {

    //atributos finais para os repositórios necessários
    private final CardRepository cardRepository;
    private final UserRepository userRepository;


    //injeção de dependência via construtor
    public CardService(CardRepository cardRepository, UserRepository userRepository) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
    }

    //método auxiliar para obter o usuário atualmente autenticado
    private User getCurrentUser() {
        //pega o nome do usuário autenticado no contexto de segurança
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        //e busca o usuário no banco de dados
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND ,"User with: " + username + " not found"));
    }

    //método para buscar todos os cards do usuário autenticado
    public ResponseEntity<List<CardDto>> findAll(){

        //busca todos os cards do usuário atualmente autenticado
        //e mapeia para DTOs
        var listCard = cardRepository.findByOwner(getCurrentUser())
            .stream().map(CardDto::new).toList();

        return ResponseEntity.ok(listCard);

    }

    //método para buscar um card pelo id
    public ResponseEntity<CardDto> findById(Long id) {

        //busca o card pelo id e mapeia para DTO
        //se não encontrar, lança uma exceção
        CardDto dto = new CardDto(
            cardRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Card with id: " + id + " not found"
                    )
            )
        );

        return ResponseEntity.ok(dto);

    }

    //método para criar um novo card
    public ResponseEntity<CardDto> createdCard(CardRequest response){

        //verifica se já existe um card com o mesmo nome para o usuário autenticado
        if(cardRepository.existsByNameAndOwner(response.name(), getCurrentUser())){
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Card with name: '" + response.name() + "' already exist"
            );
        }

        //converte a requisição em uma entidade Card e salva no repositório
        Card createdCard = cardRepository.save(toEntity(response));

        //cria a URI de localização do novo card criado
        URI location = URI.create("/cards/" + createdCard.getId());

        //retorna a resposta com o status 201 Created e o corpo contendo o DTO do card criado
        return ResponseEntity.created(location).body(
            new CardDto(createdCard)
        );

    }

    //método para editar um card existente
    public ResponseEntity<CardDto> editCard(CardDto request){


        //busca o card pelo id, lança exceção se não encontrado
        Card card = cardRepository.findById(request.id()).orElseThrow(
            () -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Card with id: " + request.id() + " not found"
            )
        );

        //atualiza os campos do card com os valores do request, se não forem nulos ou vazios
        if(request.name() != null && !request.name().isBlank()){
            card.setName(request.name());
        }

        if(request.description() != null && !request.description().isBlank()){
            card.setDescription(request.description());
        }

        if(request.ownerId() != null){

            //busca o usuário pelo id, lança exceção se não encontrado
            User user = userRepository.findById(request.ownerId()).orElseThrow(
                () -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User with id: " + request.ownerId() + " not found"
                )
            );

            card.setOwner(user);
        }

        if(request.tasks() != null){
            //converte a lista de TaskDto para um conjunto de entidades Task
            Set<Task> tasks = request.tasks().stream().map(this::toEntity).collect(Collectors.toSet());
            card.setTasks(tasks);
        }

        card = cardRepository.save(card);

        return ResponseEntity.ok(new CardDto(card));
    }

    public ResponseEntity<CardDto> deleteById(Long id){

        //busca o card pelo id, lança exceção se não encontrado
        Card card = cardRepository.findById(id).orElseThrow(
            () -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Card with id: '" + id + "' not found"
            )
        );

        //deleta o card do repositório
        //as tasks associadas são deletadas em cascata
        cardRepository.delete(card);

        return ResponseEntity.ok(new CardDto(card));

    }

    //método auxiliar para converter um CardRequest em uma entidade Card
    //o cardRequest não tem id, pois é usado para criação
    private Card toEntity(CardRequest request){

        //entidade vazia que será populada e retornada
        Card entity = new Card();

        //popula os campos da entidade com os dados da requisição
        entity.setName(request.name());
        entity.setDescription(request.description());

        entity.setOwner(getCurrentUser());
        entity.setTasks(new LinkedHashSet<>());

        return entity;

    }


    //outro método auxiliar para converter um CardDto em uma entidade Card
    //o CardDto tem id, pois é usado para edição
    //caso queira mudar o dono do card, o id do dono vem no dto por exemplo
    private Task toEntity(TaskDto dto){

        Task task = new Task();

        //busca o card pelo nome e do usuário atualmente autenticado
        Card card = cardRepository.findByNameAndOwnerUsername(dto.cardName(), getCurrentUser().getUsername()).orElseThrow(
            () -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "card with name: " + dto.cardName() + " not found"
            )
        );

        //popula os campos da entidade com os dados do DTO
        task.setCard(card);
        task.setCreatedAt(dto.createdAt());
        task.setExpectedEnd(dto.expectedEnd());
        task.setId(dto.id());
        task.setStatus(Status.valueOf(dto.status()));
        task.setName(dto.name());
        task.setDescription(dto.description());

        return task;

    }

}
