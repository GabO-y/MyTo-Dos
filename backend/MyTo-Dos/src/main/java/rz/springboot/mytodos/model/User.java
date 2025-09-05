package rz.springboot.mytodos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import rz.springboot.mytodos.model.enums.Role;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

//entidade JPA que representa um usuário no sistema
//Data é do lombok que gera getters, setters, toString, hashCode e equals
@Data
//Gera construtor com todos os argumentos
@AllArgsConstructor
//Gera construtor sem argumentos
@NoArgsConstructor
//indica que é uma entidade JPA
@Entity
//mapeia a entidade para a tabela "tb_users"
@Table(name = "tb_users")
public class User{

    //atributos da entidade
    @Id
    //define a estratégia de geração automática do ID
    //O identity é usado para bancos de dados que suportam auto-incremento
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //define a coluna como única no banco de dados
    @Column(unique = true)
    private String username;
    private String password;
    //mapeia o relacionamento um-para-muitos com a entidade Card
    //assim, um usuário pode ter vários cards
    @OneToMany(mappedBy = "owner")
    //por padrão, o Lombok inclui todos os campos no método equals e hashCode
    //mas para evitar problemas de desempenho e recursão infinita
    //é melhor excluir coleções desses métodos
    @EqualsAndHashCode.Exclude
    private Set<Card> cards;
    //mapeia o papel do usuário (Role) como uma string no banco de dados
    @Column(name = "user_role")
    private Role role;

}
