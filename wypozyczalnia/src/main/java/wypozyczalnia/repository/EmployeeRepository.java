package wypozyczalnia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wypozyczalnia.model.Address;
import wypozyczalnia.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findByAccount_Id(long id);
}
