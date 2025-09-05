package rz.springboot.mytodos.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import rz.springboot.mytodos.model.User;

import java.util.Collection;
import java.util.List;

//Classe que implementa UserDetails para integrar a entidade User com o Spring Security
public class UserAuthentication implements UserDetails {

    private final User user;

    public UserAuthentication(User user) {
        this.user = user;
    }

    //retorna as autoridades (roles) do usuário, no nosso caso, ADMIN ou BASIC
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.getRole().toString()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }
}
