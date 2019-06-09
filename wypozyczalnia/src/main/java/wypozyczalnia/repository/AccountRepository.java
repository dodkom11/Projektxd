package wypozyczalnia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wypozyczalnia.model.Account;
import wypozyczalnia.model.Task;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findByUsername(String username);
    Account findById(long id);
    Account findByEmployeeAddressNameAndEmployeeAddressSurname(String name, String surname);
    Account findByTask(Task task);
    List<Account> findAllByBracket_Id(long id);

}
