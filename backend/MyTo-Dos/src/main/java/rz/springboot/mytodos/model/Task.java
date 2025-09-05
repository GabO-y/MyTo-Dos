package rz.springboot.mytodos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rz.springboot.mytodos.model.enums.Status;

import java.time.LocalDateTime;

//entidade JPA que representa um tarrefa no sistema
//Data é do lombok que gera getters, setters, toString, hashCode e equals
@Data
//Gera construtor com todos os argumentos
@AllArgsConstructor
//Gera construtor sem argumentos
@NoArgsConstructor
//indica que é uma entidade JPA
@Entity
//mapeia a entidade para a tabela "tb_task"
@Table(name = "tb_task")
public class Task {

    //atributos da entidade
    @Id
    //define a estratégia de geração automática do ID
    //O identity é usado para bancos de dados que suportam auto-incremento
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime expectedEnd;
    //mapeia o relacionamento muitos-para-um com a entidade Card
    //assim, várias tarefas podem estar associadas a um único card
    @ManyToOne()
    @JoinColumn(name = "card_id")
    private Card card;

}
