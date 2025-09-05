package rz.springboot.mytodos.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

//classe para tratar exceções globais na aplicação  
public class GlobalHandlerException {

    //classe interna anotada com @RestControllerAdvice para capturar exceções
    @RestControllerAdvice
    public static class GlobalExceptionHandler {

        //método para tratar ResponseStatusException lançadas na aplicação
        @ExceptionHandler(ResponseStatusException.class)
        //constrói uma resposta com detalhes do erro    
        public ResponseEntity<Map<String, Object>> handleResponseStatusException(ResponseStatusException ex) {
            //cria um corpo de resposta com status, mensagem de erro e timestamp
            Map<String, Object> body = new HashMap<>();
            body.put("status", ex.getStatusCode().value());
            body.put("error", ex.getReason());
            body.put("timestamp", LocalDateTime.now().toString());

            //o map (chamado de body) permite aquela estrutura chave-valor no JSON

            //retorna a resposta com o status e o corpo de erro
            return ResponseEntity.status(ex.getStatusCode()).body(body);
        }
    }


}
