package wypozyczalnia.controller.worker;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import wypozyczalnia.config.StoredData;
import wypozyczalnia.config.VetoListener;
import wypozyczalnia.model.*;
import wypozyczalnia.model.Priority;
import wypozyczalnia.repository.*;
import wypozyczalnia.service.AccountService;
import wypozyczalnia.utils.SceneManager;
import wypozyczalnia.utils.SceneType;

import java.util.List;
import java.util.Optional;


@Controller
public class GroupsController {

    private int groupSelected;
    private int group2Selected;
    List<Bracket> brackets;
    private int state = 1;
    Button makeLeader[];
    Button delete[];

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private StateRepository stateRepository;
    @Autowired
    private PriorityRepository priorityRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private BracketRepository bracketRepository;

    @Autowired
    private AccountService accountService;

    private SceneManager sceneManager;

    private double x = 0;
    private double y = 0;
    private boolean opened = false;

    @FXML
    private ListView<HBox> listViewGroups;
    @FXML
    private ListView<Label> listOfAccounts;
    @FXML
    private ImageView undoBtn;
    @FXML
    ImageView plusBtn;
    @FXML
    private Label panelLabel;
    @FXML
    private HBox employeesBtn;

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
    void plusBtnClicked(){

    }

    @FXML
    void listViewClicked(){

    }
    @FXML
    void undoBtnClicked(){

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

        fillGroupList();

        listViewGroups.setOnMouseClicked(event -> {

            if(state==1) {
                groupSelected = listViewGroups.getSelectionModel().getSelectedIndex();
                showGroup();
            }

            if(state==2){
                for(Button btn : makeLeader){
                    btn.setVisible(false);
                }
                for(Button btn : delete){
                    btn.setVisible(false);
                }
                try {
                    makeLeader[listViewGroups.getSelectionModel().getSelectedIndex()].setVisible(true);
                    delete[listViewGroups.getSelectionModel().getSelectedIndex()].setVisible(true);
                }catch (Exception e){ }
            }
        });

        undoBtn.setOnMouseClicked(event -> {
            System.out.println("state: "  + state);
            if(state == 3){
                listOfAccounts.setVisible(false);
                plusBtn.setVisible(false);
                state=2;

            }else{
            if(state == 2) {
                    fillGroupList();
                    undoBtn.setVisible(false);

            }}
        });
    }

    @Autowired
    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }


    private void fillGroupList(){
        listViewGroups.getItems().clear();

        Account account;

        brackets = bracketRepository.findAll();
        // create hbox
        HBox hBox[] = new HBox[brackets.size()];
        // create image and vbox
        ImageView carImage[] = new ImageView[brackets.size()];
        Button btnRent[] = new Button[brackets.size()];
        VBox vBox[] = new VBox[brackets.size()];
        Label label[] = new Label[10000];

        Image image = new Image("/icons/icons8-user-account-100.png");


        int x = 0;
        int y = 0;
        for(Bracket bracket : brackets) {

            vBox[x] = new VBox();
            hBox[x] = new HBox();
            carImage[x] = new ImageView();


            label[x*y] = new Label("  Group ID : " + bracket.getId());

            String name = accountRepository.getOne(bracket.getLeader()).getEmployee().getAddress().getName();
            String surname = accountRepository.getOne(bracket.getLeader()).getEmployee().getAddress().getSurname();
            label[x*y + 1] = new Label("  Group Leader: " + name + " " + surname);
            label[x*y + 2] = new Label("  Others: ");
            List<Account> accounts =  accountRepository.findAllByBracket_Id(bracket.getId());
            y += accounts.size()+3;



            int z = 3;
            for(Account acc : accounts){

                if(acc.getBracket().getLeader() != acc.getId()){
                    System.out.println("leader leader leader : " + acc.getBracket().getLeader());
                    System.out.println(acc.getId());
                    label[x*y + z] = new Label("  " + acc.getEmployee().getAddress().getName() + " " +
                            acc.getEmployee().getAddress().getSurname());
                    z++;
                }
            }



            // fill image and vbox
            carImage[x].setImage(image);

            for(int i = 0; i < y-1; i++){
                System.out.println(y+3);
                System.out.println(label[x*y + i].getText());
                vBox[x].getChildren().add(label[x*y+i]);
            }

            // add image and vbox to hbox
            hBox[x].getChildren().add(carImage[x]);
            hBox[x].getChildren().add(vBox[x]);

            // add hbox to listview
            listViewGroups.getItems().add(hBox[x]);
            state = 1;
            x++;
        }


    }

    private void showGroup(){
        listViewGroups.getItems().clear();

        List<Account> accounts = accountRepository.findAllByBracket_Id(brackets.get(groupSelected).getId());

        HBox hBox[] = new HBox[accounts.size()];
        Label label[] = new Label[accounts.size()];
        Label leader = new Label(" Leader  ");
        leader.setStyle("-fx-text-fill: red; -fx-font-size: 16");
        makeLeader = new Button[accounts.size()];
        delete = new Button[accounts.size()];
        HBox addBox = new HBox();
        Button add = new Button("      Add      ");
        add.setOnMouseClicked(event -> addPerson(accounts));
        addBox.getChildren().add(add);

        int x = 0;
        for(Account acc : accounts){
            hBox[x] = new HBox();
            label[x] = new Label(accounts.get(x).getEmployee().getAddress().getName() + " " + accounts.get(x).getEmployee().getAddress().getSurname() + "  ");

            makeLeader[x] = new Button("Leader");
            makeLeader[x].setOnMouseClicked(event -> makeLeaderClicked(acc));
            makeLeader[x].setVisible(false);
            delete[x] = new Button("Delete");
            delete[x].setOnMouseClicked(event -> deleteFromGroup(acc));
            delete[x].setVisible(false);
            label[x].setStyle("-fx-font-size: 16");

            hBox[x].getChildren().add(label[x]);
            if(acc.getId() == brackets.get(groupSelected).getLeader()){
                hBox[x].getChildren().add(leader);
            }else{
                hBox[x].getChildren().add(makeLeader[x]);
            }

            hBox[x].getChildren().add(delete[x]);
            listViewGroups.getItems().add(hBox[x]);
            x++;
        }
        listViewGroups.getItems().add(addBox);

        state=2;
        opened = true;
        undoBtn.setVisible(true);

    }

    private void makeLeaderClicked(Account acc){
        System.out.println(acc.getEmployee().getAddress().getName());
        Bracket bracket = bracketRepository.getOne(brackets.get(groupSelected).getId());
        bracket.setLeader(acc.getId());
        bracketRepository.save(bracket);
        brackets = bracketRepository.findAll();
        System.out.println("state: " + state);
        showGroup();
    }

    private void deleteFromGroup(Account acc){
        /*acc.setBracket(bracketRepository.getOne(1337l));
        accountRepository.save(acc);
        brackets = bracketRepository.findAll();
        showGroup();*/
    }
    private void addPerson(List<Account> accounts){
        listOfAccounts.getItems().clear();
        listOfAccounts.setVisible(true);
        plusBtn.setVisible(true);
        Label label[] = new Label[accounts.size()];

        int z = 0;
        for(Account acc : accounts){
            label[z] = new Label(acc.getEmployee().getAddress().getName() + " " + acc.getEmployee().getAddress().getSurname());
            listOfAccounts.getItems().add(label[z]);

            z++;
        }
        state=3;
    }
}
