package wypozyczalnia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wypozyczalnia.model.Address;
import wypozyczalnia.model.Bracket;
import wypozyczalnia.model.Task;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByBracket_Id(long id);
    Task findByTitle(String title);
    Task findByBracket_Id(long id);
    Task findByBracket(Bracket bracket);

}
