package rz.springboot.mytodos.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import rz.springboot.mytodos.model.Card;
import rz.springboot.mytodos.model.User;

import java.util.List;
import java.util.Optional;

//repositório JPA para a entidade Card
//fornece métodos para operações de CRUD e consultas personalizadas
//Acho interressante essa parte, pois as proprías queries a partir
//dos nomes dos atributos
// find...By: Inicia a query.
// By: A partir daqui, você especifica os atributos da entidade.
// And, Or: Usados para combinar múltiplas condições.
// GreaterThan, LessThan: Para comparações numéricas.
// Containing, Like: Para buscas de strings.
// OrderBy...Asc, OrderBy...Desc: Para ordenação dos resultados.
public interface CardRepository extends JpaRepository<Card, Long> {
    Optional<Card> findByName(String name);
    Optional<Card> findByNameAndOwnerUsername(String name, String username);
    Page<Card> findAll(Pageable pageable);
    List<Card> findByOwner(User owner);
    boolean existsByNameAndOwner(String name, User owner);
}
