package rz.springboot.mytodos.controller;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rz.springboot.mytodos.model.dto.CardDto;
import rz.springboot.mytodos.model.request.CardRequest;
import rz.springboot.mytodos.service.CardService;

import java.util.List;

//controlador REST para gerenciar cards
//é anotada com @RestController para ser reconhecida como um controlador REST pelo Spring
//e com @RequestMapping("/cards") para mapear as requisições HTTP para o
@RestController
@RequestMapping("/cards")
public class CardController {

    //atributo para o serviço dos cards
    private final CardService cardService;

    //injeção de dependência via construtor
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    //endpoints para operações CRUD de cards
    @GetMapping()
    public ResponseEntity<List<CardDto>> findAll(){
        return cardService.findAll();
    }

    //endpoint para criar um novo card
    //o request body é para capturar os dados do card no corpo da requisição
    @PostMapping()
    public ResponseEntity<CardDto> createCard(@RequestBody CardRequest card){
        return cardService.createdCard(card);
    }

    //endpoint para buscar um card pelo id
    //o variable path é para capturar o id do card na URL
    @GetMapping("/{id}")
    public ResponseEntity<CardDto> findById(@PathVariable Long id){
        return cardService.findById(id);
    }

    //endpoint para editar um card existente
    @PutMapping()
    public ResponseEntity<CardDto> editCard(@RequestBody CardDto dto){
        return cardService.editCard(dto);
    }

    //endpoint para deletar um card pelo id
    @DeleteMapping("/{id}")
    public ResponseEntity<CardDto> deleteCard(@PathVariable Long id) {
        return cardService.deleteById(id);
    }

}
