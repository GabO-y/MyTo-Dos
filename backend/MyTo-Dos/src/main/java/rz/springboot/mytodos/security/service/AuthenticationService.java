package rz.springboot.mytodos.security.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;
import rz.springboot.mytodos.model.User;
import rz.springboot.mytodos.model.enums.Role;
import rz.springboot.mytodos.repository.UserRepository;
import rz.springboot.mytodos.security.request.AuthRequest;

import java.util.Map;

//serviço para autenticação e registro de usuários
@Service
public class AuthenticationService {

    //atributos finais para os serviços e repositórios necessários
    private final JtwService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    //injeção de dependência via construtor
    public AuthenticationService(JtwService jwtService, UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    //método auxiliar para autenticar um usuário e gerar um token JWT
    public String authenticate(AuthRequest request){

        //tenta autenticar o usuário com o username e password fornecidos
        var authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.username(),request.password())
        );

        //se a autenticação for bem-sucedida, gera e retorna um token JWT
        return jwtService.generateToken(authentication);
    }

    //método para registrar um novo usuário
    public ResponseEntity<?> register(@RequestBody AuthRequest request) {

        //verifica se o username já existe ou se os campos são inválidos
        if (userRepository.findByUsername(request.username()).isPresent() || request.username().isBlank() || request.password().isBlank()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST
                ,"username: '" + request.username() + "' invalid, try another"
            );
        }

        //cria um novo usuário com o username e password fornecidos
        User user = new User();
        user.setUsername(request.username());
        //e codifica a senha usando o PasswordEncoder
        user.setPassword(passwordEncoder.encode(request.password()));
        //define o papel padrão como BASIC
        user.setRole(Role.BASIC);

        userRepository.save(user);

        //autentica o usuário recém-registrado e gera um token JWT
        String token = authenticate(request);

        //e retorna o token na resposta
        return ResponseEntity.ok(Map.of("token", token));
    }


}
