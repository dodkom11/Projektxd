package wypozyczalnia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import wypozyczalnia.config.StoredData;
import wypozyczalnia.model.Account;
import wypozyczalnia.repository.AccountRepository;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public String login(String username, String password) {


            Account account = accountRepository.findByUsername(username);

            if (account != null && passwordEncoder.matches(password, account.getPassword())) {
                StoredData.setLoggedUserId(account.getId());
                return account.getPermission().getName();

            }
            return "error";
    }
}
