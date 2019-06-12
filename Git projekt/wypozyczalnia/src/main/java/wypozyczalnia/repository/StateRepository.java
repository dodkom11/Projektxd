package wypozyczalnia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wypozyczalnia.model.Address;
import wypozyczalnia.model.State;

@Repository
public interface StateRepository extends JpaRepository<State, Long> {
    State findByName(String name);
}
