package wypozyczalnia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wypozyczalnia.model.Address;
import wypozyczalnia.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    void deleteById(Long id);
}
