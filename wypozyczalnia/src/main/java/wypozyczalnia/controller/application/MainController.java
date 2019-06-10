package wypozyczalnia.controller.application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import wypozyczalnia.service.AccountService;
import wypozyczalnia.utils.SceneManager;
import javafx.scene.input.MouseEvent;
import wypozyczalnia.utils.SceneType;

@Controller
public class MainController {
    private SceneManager sceneManager;

    private double x = 0;
    private double y = 0;

    @Autowired
    private AccountService accountService;

    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Label failSignInText;

    @FXML
    void dragged(MouseEvent event){
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();

        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);
        stage.setOpacity(0.5);
    }
    @FXML
    void released(MouseEvent event){
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();

        stage.setOpacity(1);
    }
    @FXML
    void pressed(MouseEvent event){
        x = event.getSceneX();
        y = event.getSceneY();
    }

    @FXML
    void closeBtnClicked(MouseEvent event){
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    @FXML
    void minimizeBtnClicked(MouseEvent event){
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    void usernameFieldClicked(MouseEvent event){
        failSignInText.setVisible(false);
    }

    @FXML
    void passwordFieldClicked(MouseEvent event){
        failSignInText.setVisible(false);
    }

    @FXML
    public void onEnter(ActionEvent event){
        signIn();
    }

    @FXML
    void signInBtnClicked(MouseEvent event){
        signIn();
    }

    private void signIn(){
        String permissions = accountService.login(username.getText(), password.getText());
        System.out.println("perrmissions: " + permissions);
        switch (permissions){
            case "worker":
                sceneManager.show(SceneType.WORKER_MAIN);
                break;
            case "admin":
                sceneManager.show(SceneType.ADMIN_GROUPS);
                break;
            default:
                failSignInText.setVisible(true);
        }
    }
    @FXML
    public void initialize() {
    }

    @Autowired
    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }
}
