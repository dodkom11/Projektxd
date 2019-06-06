package wypozyczalnia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wypozyczalnia.model.Address;
import wypozyczalnia.model.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
