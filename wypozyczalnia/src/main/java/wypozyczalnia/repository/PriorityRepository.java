package wypozyczalnia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wypozyczalnia.model.Address;
import wypozyczalnia.model.Priority;

@Repository
public interface PriorityRepository extends JpaRepository<Priority, Long> {
    String findById(long id);
    Priority findByName(String name);
}
