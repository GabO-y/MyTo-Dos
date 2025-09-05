package rz.springboot.mytodos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rz.springboot.mytodos.model.User;

import java.util.Optional;


//repositório JPA para a entidade User
//fornece um método para operações de CRUD e consultas personalizadas
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

}
