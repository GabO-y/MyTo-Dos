package rz.springboot.mytodos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;
//entidade JPA que representa um card no sistema
//indica que é uma entidade JPA
@Entity
//Data é do lombok que gera getters, setters, toString, hashCode e equals
@Data
//Gera construtor com todos os argumentos
@AllArgsConstructor
//Gera construtor sem argumentos
@NoArgsConstructor
//mapeia a entidade para a tabela "tb_cards"
@Table(name = "tb_cards")
public class Card {

    //atributos da entidade
    @Id
    //define a estratégia de geração automática do ID
    //O identity é usado para bancos de dados que suportam auto-incremento
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

    //mapeia o relacionamento um-para-muitos com a entidade Task
    //assim, um card pode ter várias tarefas
    //fetch EAGER carrega as tarefas junto com o card
    //cascade ALL propaga todas as operações (persistir, remover, etc.) para as tarefas
    //orphanRemoval true remove tarefas órfãs quando removidas do card
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    //exclui a coleção do equals e hashCode para evitar problemas de desempenho e recursão infinita
    @EqualsAndHashCode.Exclude
    private Set<Task> tasks;
    //mapeia o relacionamento muitos-para-um com a entidade User
    //assim, vários cards podem estar associados a um único usuário (dono)
    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User owner;

}
