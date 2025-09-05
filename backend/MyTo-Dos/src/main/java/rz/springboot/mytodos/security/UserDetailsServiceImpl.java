package rz.springboot.mytodos.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import rz.springboot.mytodos.model.User;
import rz.springboot.mytodos.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    
    private final UserRepository userRepository;

    //Injeção de dependência via construtor de UserRepository
    //já que o autowired não é recomendado em atributos
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    //carrega o usuário pelo username
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //busca o usuário no banco de dados
        return userRepository.findByUsername(username).map(
            UserAuthentication::new
        ).orElseThrow(

        //lança uma exceção falando o nome enviado caso o usuário não seja encontrado
            () -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "User with username: '" + username + "' not found"
            )

        );

    }
}
