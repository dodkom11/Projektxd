package wypozyczalnia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wypozyczalnia.model.Address;
import wypozyczalnia.model.Bracket;

@Repository
public interface BracketRepository extends JpaRepository<Bracket, Long> {
}
