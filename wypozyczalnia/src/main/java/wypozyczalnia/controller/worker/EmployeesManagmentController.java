package wypozyczalnia.controller.worker;


import javafx.fxml.FXML;

import javafx.geometry.HPos;

import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import wypozyczalnia.config.StoredData;
import wypozyczalnia.model.*;
import wypozyczalnia.repository.*;
import wypozyczalnia.security.Encryption;
import wypozyczalnia.service.AccountService;
import wypozyczalnia.utils.SceneManager;
import javafx.scene.input.MouseEvent;
import wypozyczalnia.utils.SceneType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class EmployeesManagmentController {
    private SceneManager sceneManager;

    private double x = 0;
    private double y = 0;
    private int listViewSelectedIndex;
    private boolean edit = false;

    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private BracketRepository bracketRepository;
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private Encryption encryption;

    List<Employee> employees;

    Employee employee;

    @FXML
    private Pane addClientPane;
    @FXML
    private AnchorPane clientPane;
    @FXML
    private Button addClientBtn;
    @FXML
    private TextField name;
    @FXML
    private TextField surname;
    @FXML
    private TextField pesel;
    @FXML
    private TextField city;
    @FXML
    private TextField street;
    @FXML
    private TextField houseNumber;
    @FXML
    private TextField zipCode;
    @FXML
    private TextField telNumber;
    @FXML
    private TextField eMail;
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private TextField salary;
    @FXML
    private TextField position;

    @FXML
    private ListView<HBox> listViewPane;
    @FXML
    private Button editClientBtn;
    @FXML
    private Button deleteClientBtn;
    @FXML
    private Label panelLabel;
    @FXML
    private Label mainTitleLabel;
    @FXML
    private Label groupTask;
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
    void logoutClicked(){
        sceneManager.show(SceneType.MAIN);
    }
    @FXML
    void tasksClicked(){
        if(StoredData.isAdmin()){
            sceneManager.show(SceneType.ADMIN_GROUPS);
        }else{

            sceneManager.show(SceneType.WORKER_MAIN);
        }
    }
    @FXML
    void ourCarsClicked(){
        sceneManager.show(SceneType.WORKER_OUR_CARS);
    }
    @FXML
    void employeesBtnClicked(){sceneManager.show(SceneType.ADMIN_EMPLOYEE_MANAGMENT);}
    @FXML
    void rentClicked(){ sceneManager.show(SceneType.WORKER_RENT_CAR); }
    @FXML
    void diagramsClicked(){
        sceneManager.show(SceneType.WORKER_DIAGRAMS);
    }
    @FXML
    void clientsClicked() { sceneManager.show(SceneType.WORKER_CLIENT_MANAGEMENET);}


    @FXML
    public void initialize() {
        if(StoredData.isAdmin()){
            employeesBtn.setVisible(true);
            groupTask.setText("Groups");
            panelLabel.setText("Admin Panel");
        }else{
            panelLabel.setText("Worker Panel");
            employeesBtn.setVisible(false);
            groupTask.setText("Tasks");
        }

        fillClientData();

    }
    @FXML
    void addClientClicked(){
        listViewPane.setVisible(false);
        addClientBtn.setVisible(false);
        editClientBtn.setVisible(false);
        deleteClientBtn.setVisible(false);

        addClientPane.setVisible(true);

    }
    @FXML
    void confirmAddClicked(){

        if(edit) {
            Address address = addressRepository.getOne(employee.getAddress().getId());
            address.setName(name.getText());
            address.setSurname(surname.getText());
            address.setPesel(pesel.getText());
            address.setCity(city.getText());
            address.setStreet(street.getText());
            address.setHouseNumber(Integer.valueOf(houseNumber.getText()));
            address.setZipCode(zipCode.getText());
            address.setTelephoneNumber(Long.valueOf(telNumber.getText()));
            address.setEmail(eMail.getText());
            addressRepository.save(address);

            Account account = accountRepository.getOne(employee.getAccount().getId());
            account.setUsername(username.getText());
            if(!username.getText().equals("")){
                account.setPassword(encryption.encode(password.getText()));
            }
            accountRepository.save(account);

            employee.setPosition(position.getText());
            employee.setSalary(Double.valueOf(salary.getText()));
            employeeRepository.save(employee);

        }else{
            Address address = new Address(name.getText(), surname.getText(), pesel.getText(), city.getText(),
                    street.getText(), Integer.valueOf(houseNumber.getText()), zipCode.getText(), Long.valueOf(telNumber.getText()), eMail.getText());
            addressRepository.save(address);

            Account account = new Account();
            account.setBracket(bracketRepository.getOne(1l));
            account.setPermission(permissionRepository.getOne(1l));
            account.setPassword(encryption.encode(password.getText()));
            account.setUsername(username.getText());
            accountRepository.save(account);

            Employee employee = new Employee();
            employee.setAddress(address);
            employee.setPosition(position.getText());
            employee.setEmploymentDate(new Date());
            employee.setSalary(Double.valueOf(salary.getText()));
            employee.setAccount(account);
            employeeRepository.save(employee);
        }

        fillClientData();

        addClientPane.setVisible(false);
        listViewPane.setVisible(true);
        addClientBtn.setVisible(true);

        editClientBtn.setVisible(true);
        deleteClientBtn.setVisible(true);


        edit = false;

        name.clear();
        surname.clear();
        eMail.clear();
        city.clear();
        pesel.clear();
        houseNumber.clear();
        zipCode.clear();
        telNumber.clear();
        street.clear();
        username.clear();
        position.clear();
        salary.clear();
        password.clear();


    }

    @FXML
    void listViewClicked(){
        listViewSelectedIndex = listViewPane.getSelectionModel().getSelectedIndex();

    }
    @FXML
    void editClientClicked(){
        if(StoredData.isAdmin()){
            employee = employeeRepository.getOne(employees.get(listViewSelectedIndex).getId());

            name.setText(employee.getAddress().getName());
            surname.setText(employee.getAddress().getSurname());
            eMail.setText(employee.getAddress().getEmail());
            city.setText(employee.getAddress().getCity());
            pesel.setText(employee.getAddress().getPesel());
            houseNumber.setText(String.valueOf(employee.getAddress().getHouseNumber()));
            zipCode.setText(employee.getAddress().getZipCode());
            telNumber.setText(String.valueOf(employee.getAddress().getTelephoneNumber()));
            street.setText(employee.getAddress().getStreet());
            username.setText(employee.getAccount().getUsername());
            salary.setText(String.valueOf(employee.getSalary()));
            position.setText(String.valueOf(employee.getSalary()));
            password.setText("");

            listViewPane.setVisible(false);
            addClientBtn.setVisible(false);

            editClientBtn.setVisible(false);
            deleteClientBtn.setVisible(false);


            addClientPane.setVisible(true);
            edit = true;
        }else{
            accountService.showError();
        }

    }
    @FXML
    void deleteClientClicked(){



        if(StoredData.isAdmin()){

            Employee deleteEmployee = employeeRepository.getOne(employees.get(listViewSelectedIndex).getId());
            boolean canDelete = true;
            List<Bracket> allBrackets = bracketRepository.findAll();

            for(Bracket br : allBrackets){
                if(br.getLeader() == deleteEmployee.getAccount().getId()){
                    canDelete = false;
                }
            }

            if(canDelete){
                Account account = accountRepository.getOne(deleteEmployee.getAccount().getId());
                Address address = addressRepository.getOne(deleteEmployee.getAddress().getId());


                employeeRepository.delete(deleteEmployee);
                accountRepository.delete(account);
                addressRepository.delete(address);


                fillClientData();
            }else{
                accountService.showDeleteError();
            }
        }else{
            accountService.showError();
        }
    }

    @Autowired
    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }



    private void fillClientData(){
        listViewPane.getItems().clear();


        employees = employeeRepository.findAll();


        Label label[] = new Label[employees.size()*6];
        HBox hBox[] = new HBox[employees.size()*6];
        HBox hBoxBig[] = new HBox[employees.size()];
        VBox vBox[] = new VBox[employees.size()];
        HBox hBoxForImage[] = new HBox[employees.size()];

        Label idLabel[] = new Label[employees.size()];
        Label nameLabel[] = new Label[employees.size()];
        Label surnameLabel[] = new Label[employees.size()];
        Label emailLabel[] = new Label[employees.size()];
        Label phoneLabel[] = new Label[employees.size()];

        ImageView imagePerson[] = new ImageView[employees.size()];
        Image image = new Image("/icons/icons8-customer-100.png");

        int x = 0;
        int y = 0;
        int z = 0;


        for(Employee cli : employees){

            idLabel[y] = new Label("ID: ");
            idLabel[y].setStyle("-fx-font-weight: bold");
            nameLabel[y] = new Label("Name: ");
            nameLabel[y].setStyle("-fx-font-weight: bold");
            surnameLabel[y] = new Label("Surname: ");
            surnameLabel[y].setStyle("-fx-font-weight: bold");
            emailLabel[y] = new Label("E-Mail: ");
            emailLabel[y].setStyle("-fx-font-weight: bold");
            phoneLabel[y] = new Label("Tel-Number: ");
            phoneLabel[y].setStyle("-fx-font-weight: bold");

            //creating boxes for labels
            hBox[x] = new HBox();
            hBox[x+1] = new HBox();
            hBox[x+2] = new HBox();
            hBox[x+3] = new HBox();
            hBox[x+4] = new HBox();

            // creating labels of client data
            label[x] = new Label(cli.getId().toString());
            label[x+1] = new Label(cli.getAddress().getName());
            label[x+2] = new Label(cli.getAddress().getSurname());
            label[x+3] = new Label(cli.getAddress().getEmail());
            label[x+4] = new Label(cli.getAddress().getTelephoneNumber().toString());

            // adding first label to first HBox
            hBox[x].getChildren().add(idLabel[y]);
            hBox[x].getChildren().add(label[x]);
            // adding second label to second HBox
            hBox[x+1].getChildren().add(nameLabel[y]);
            hBox[x+1].getChildren().add(label[x+1]);
            // adding third label to third HBox
            hBox[x+2].getChildren().add(surnameLabel[y]);
            hBox[x+2].getChildren().add(label[x+2]);
            // adding fourth label to fourth HBox
            hBox[x+3].getChildren().add(emailLabel[y]);
            hBox[x+3].getChildren().add(label[x+3]);
            // adding fifth label to fifth HBox
            hBox[x+4].getChildren().add(phoneLabel[y]);
            hBox[x+4].getChildren().add(label[x+4]);

            //creating VBox for HBoxes
            vBox[y] = new VBox();
            // adding Hboxes to VBox
            vBox[y].getChildren().add(hBox[x]);
            vBox[y].getChildren().add(hBox[x+1]);
            vBox[y].getChildren().add(hBox[x+2]);
            vBox[y].getChildren().add(hBox[x+3]);
            vBox[y].getChildren().add(hBox[x+4]);

            hBoxForImage[y] = new HBox();
            imagePerson[y] = new ImageView();
            hBoxBig[z] = new HBox();

            imagePerson[y].setImage(image);


            hBoxForImage[y].getChildren().add(imagePerson[y]);
            hBoxForImage[y].getChildren().add(vBox[y]);

            hBoxBig[z].getChildren().add(hBoxForImage[y]);
            listViewPane.getItems().add(hBoxForImage[y]);

            if(y%2 == 1){


                z++;
            }

            y++;
            x+=5;


        }


        // listViewPane.getItems().add(hBoxBig);

    }
}
