package wypozyczalnia.controller.worker;

import javafx.fxml.FXML;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import wypozyczalnia.config.StoredData;
import wypozyczalnia.model.*;
import wypozyczalnia.repository.*;
import wypozyczalnia.utils.SceneManager;
import wypozyczalnia.utils.SceneType;

import java.util.List;


@Controller
public class GroupsController {

    List<Bracket> allBrackets;


    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private BracketRepository bracketRepository;


    private SceneManager sceneManager;

    private double x = 0;
    private double y = 0;

    @FXML
    private ListView<HBox> listViewGroups;
    @FXML
    private Label panelLabel;
    @FXML
    private HBox employeesBtn;
    @FXML
    private ListView<Label> peopleInGroup;

    @FXML
    void dragged(MouseEvent event){
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();

        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);
        stage.setOpacity(0.5);
    }


    @FXML
    void pressed(MouseEvent event){
        x = event.getSceneX();
        y = event.getSceneY();

    }
    @FXML
    void released(MouseEvent event){
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.setOpacity(1);
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
    void addGroupClicked(){
        Bracket bracket = new Bracket();
        bracket.setLeader(1L);
        bracketRepository.save(bracket);
        fillList();
    }


    @FXML
    void logoutClicked(){ sceneManager.show(SceneType.MAIN); }
    @FXML
    void clientsClicked(){
        sceneManager.show(SceneType.WORKER_CLIENT_MANAGEMENET);
    }
    @FXML
    void ourCarsClicked(){
        sceneManager.show(SceneType.WORKER_OUR_CARS);
    }
    @FXML
    void rentCarClicked(){
        sceneManager.show(SceneType.WORKER_RENT_CAR);
    }
    @FXML
    void diagramsClicked(){
        sceneManager.show(SceneType.WORKER_DIAGRAMS);
    }
    @FXML
    void employeesBtnClicked(){sceneManager.show(SceneType.ADMIN_EMPLOYEE_MANAGMENT);}

    @FXML
    public void initialize() {

        if(StoredData.isAdmin()){
            employeesBtn.setVisible(true);
            panelLabel.setText("Admin Panel");
        }else{
            panelLabel.setText("Worker Panel");
            employeesBtn.setVisible(false);
        }

        fillList();


        listViewGroups.setOnMouseClicked(event -> {
            fillSecondList();

            peopleInGroup.setVisible(true);
        });

        peopleInGroup.setOnMouseClicked(event -> {
            changeLeader();
            peopleInGroup.setVisible(false);
        });



    }

    @Autowired
    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }


    private void fillList(){
        listViewGroups.getItems().clear();
        allBrackets = bracketRepository.findAll();

        Label id[] = new Label[allBrackets.size()];
        Label label[] = new Label[allBrackets.size()];
        Label label2[] = new Label[allBrackets.size()];
        ImageView imagePerson[] = new ImageView[allBrackets.size()];
        Image image = new Image("/icons/icons8-user-account-100.png");
        HBox hBox[] = new HBox[allBrackets.size()];
        VBox vBox[] = new VBox[allBrackets.size()];

        int z = 0;
        for(Bracket bracket : allBrackets){
            id[z] = new Label("Group ID:  " + bracket.getId());
            label[z] = new Label("Group leader: ");
            String name = accountRepository.getOne(bracket.getLeader()).getEmployee().getAddress().getName();
            String surname = accountRepository.getOne(bracket.getLeader()).getEmployee().getAddress().getSurname();
            label2[z] = new Label( name + " " + surname);
            hBox[z] = new HBox();
            imagePerson[z] = new ImageView(image);
            vBox[z] = new VBox();
            vBox[z].getChildren().add(id[z]);
            vBox[z].getChildren().add(label[z]);
            vBox[z].getChildren().add(label2[z]);


            hBox[z].getChildren().add(imagePerson[z]);
            hBox[z].getChildren().add(vBox[z]);
            listViewGroups.getItems().add(hBox[z]);

        }


    }

    private void fillSecondList(){
        peopleInGroup.getItems().clear();
        Bracket bracket = allBrackets.get(listViewGroups.getSelectionModel().getSelectedIndex());

        List<Account> accounts = accountRepository.findAllByBracket_Id(bracket.getId());

        int x = 0;
        for(Account acc : accounts){
            String name = acc.getEmployee().getAddress().getName();
            String surname = acc.getEmployee().getAddress().getSurname();
            if(bracket.getLeader() == acc.getId()){
                peopleInGroup.getItems().add(new Label(name + " " + surname + " - Leader"));
            }else{
                peopleInGroup.getItems().add(new Label(name + " " + surname));
            }
        }
    }

    private void changeLeader(){

        Bracket bracket = allBrackets.get(listViewGroups.getSelectionModel().getSelectedIndex());
        List<Account> accounts = accountRepository.findAllByBracket_Id(bracket.getId());

        bracket.setLeader(accounts.get(peopleInGroup.getSelectionModel().getSelectedIndex()).getId());
        bracketRepository.save(bracket);

        fillSecondList();
        fillList();

    }


}
