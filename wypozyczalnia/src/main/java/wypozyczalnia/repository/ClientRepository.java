package wypozyczalnia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wypozyczalnia.model.Address;

@Repository
public interface ClientRepository extends JpaRepository<Address, Long> {
}
