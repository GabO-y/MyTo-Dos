package rz.springboot.mytodos.security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rz.springboot.mytodos.security.request.AuthRequest;
import rz.springboot.mytodos.security.service.AuthenticationService;

import java.util.Map;

//controlador REST para autenticação e registro de usuários

@RestController
@RequestMapping("/auth")
public class AccessController {

    //atributo para o serviço de autenticação
    private final AuthenticationService authenticationService;


    //injeção de dependência via construtor
    public AccessController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    //endpoint para autenticação de usuários
    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthRequest request){
        //o request tem o username e password
        String token = authenticationService.authenticate(request);
        //e retorna o token JWT na resposta: { "token": "valor_do_token" }
        return ResponseEntity.ok(Map.of("token", token));
    }

    //endpoint para registro de novos usuários  
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest request) {
        //tambem retorna o token JWT na resposta
        return authenticationService.register(request);
    }
}
