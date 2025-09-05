package rz.springboot.mytodos.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import rz.springboot.mytodos.model.Task;

//repositório JPA para a entidade Task
//fornece métodos para operações de CRUD e consultas personalizadas
public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findAll(Pageable pageable);

    Page<Task> findByCardId(Long id, Pageable pageable);

}
