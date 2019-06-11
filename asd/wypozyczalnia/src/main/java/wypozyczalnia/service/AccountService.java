package wypozyczalnia.service;

import javafx.scene.control.Alert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import wypozyczalnia.config.StoredData;
import wypozyczalnia.model.Account;
import wypozyczalnia.model.Bracket;
import wypozyczalnia.model.Task;
import wypozyczalnia.repository.AccountRepository;
import wypozyczalnia.repository.BracketRepository;
import wypozyczalnia.repository.TaskRepository;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private BracketRepository bracketRepository;
    @Autowired
    private TaskRepository taskRepository;


    public String login(String username, String password) {


            Account account = accountRepository.findByUsername(username);
            Bracket bracket = bracketRepository.getOne(account.getBracket().getId());

            if (account != null && passwordEncoder.matches(password, account.getPassword())) {
                if(account.getId() == bracket.getLeader()){
                    StoredData.setLeader(true);
                }else{
                    StoredData.setLeader(false);
                }
                if(account.getPermission().getName().equals("admin")){
                    StoredData.setAdmin(true);
                }else{
                    StoredData.setAdmin(false);
                }
                StoredData.setLoggedUserId(account.getId());
                return account.getPermission().getName();
            }
            return "error";
    }

    public void showError(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Access Error");
        alert.setHeaderText("You don't have access to this functionality");
        alert.setContentText("Only Admin can do that");

        alert.showAndWait();
    }

    public void showDeleteError(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Delete Error");
        alert.setHeaderText("You can't delete this employee");
        alert.setContentText("in order to remove, you need to give Leader to another person");

        alert.showAndWait();
    }
    public void fillFieldError(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Something went wrong :(");
        alert.setContentText("Please complete fields correctly, and try again!");

        alert.showAndWait();
    }
    public void simpleError(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("In order to change or delete, you need to select it");
        alert.setContentText("Try Again!");

        alert.showAndWait();
    }

    public void saveTask(Task task){
        taskRepository.save(task);
    }
}
