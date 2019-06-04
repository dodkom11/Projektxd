package wypozyczalnia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wypozyczalnia.model.Account;
import wypozyczalnia.repository.AccountRepository;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;


    public void login(String username, String password) {



            Account account = accountRepository.findByUsername(username);
            if(account == null){
                System.out.println("tak");
            }else{
                System.out.println("nie");
            }

    }

    
}
