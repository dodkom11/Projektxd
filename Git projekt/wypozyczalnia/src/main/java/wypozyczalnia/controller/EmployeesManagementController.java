package wypozyczalnia.controller;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;

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
public class EmployeesManagementController {
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
    private PasswordField password;
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
    private Label groupTask;
    @FXML
    private HBox employeesBtn;
    @FXML
    private ComboBox<String> groupCombo;
    @FXML
    private Button confirmBtn;
    @FXML
    private Label labelError;


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
    void employeesBtnClicked(){sceneManager.show(SceneType.ADMIN_EMPLOYEE_MANAGEMENT);}
    @FXML
    void rentClicked(){ sceneManager.show(SceneType.WORKER_RENT_CAR); }
    @FXML
    void diagramsClicked(){
        sceneManager.show(SceneType.WORKER_DIAGRAMS);
    }
    @FXML
    void clientsClicked() { sceneManager.show(SceneType.WORKER_CLIENT_MANAGEMENT);}


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


        confirmBtn.disableProperty()
                .bind(name.textProperty().isEmpty()
                        .or(surname.textProperty().isEmpty())
                        .or(pesel.textProperty().isEmpty())
                        .or(city.textProperty().isEmpty())
                        .or(street.textProperty().isEmpty())
                        .or(zipCode.textProperty().isEmpty())
                        .or(houseNumber.textProperty().isEmpty())
                        .or(telNumber.textProperty().isEmpty())
                        .or(eMail.textProperty().isEmpty())
                );

        zipCode.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*(\\-\\d*)?")) {
                    zipCode.setText(oldValue);
                }
            }
        });

        fillClientData();

    }
    @FXML
    void addClientClicked(){
        List<Bracket> allBrackets = bracketRepository.findAll();
        for(Bracket br : allBrackets){
            groupCombo.getItems().add(String.valueOf(br.getId()));
        }
        listViewPane.setVisible(false);
        addClientBtn.setVisible(false);
        editClientBtn.setVisible(false);
        deleteClientBtn.setVisible(false);

        addClientPane.setVisible(true);

    }

    /**
     *
     * @return value that give use information if we can click confirm button or not
     */
    private boolean canEdit(List<String> textField) {
        boolean canEdit = true;
        for (String tx : textField) {
            if (tx.equals("")) {
                canEdit = false;
            }
        }
        return canEdit;
    }


    @FXML
    void confirmAddClicked(){
        List<String> tx= new ArrayList<>();
        tx.add(name.getText());
        tx.add(surname.getText());
        tx.add(pesel.getText());
        tx.add(city.getText());
        tx.add(street.getText());
        tx.add(houseNumber.getText());
        tx.add(zipCode.getText());
        tx.add(telNumber.getText());
        tx.add(eMail.getText());
        if(!groupCombo.getSelectionModel().getSelectedItem().isEmpty()){
            tx.add(groupCombo.getSelectionModel().getSelectedItem());
        }

        if(pesel.getText().length() != 11){
            labelError.setText("pesel is incorrect");
            labelError.setVisible(true);
        }else {
            String passwd = password.getText();
            String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=.])(?=\\S+$).{8,}";
            System.out.println(passwd.matches(pattern));
            if(!passwd.matches(pattern)){
                    labelError.setText("password is too weak");
                    labelError.setVisible(true);
            }else{


            labelError.setVisible(false);
            if (canEdit(tx)) {
                if (edit) {
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
                    if (!username.getText().isEmpty()) {
                        account.setPassword(encryption.encode(password.getText()));
                    }
                    account.setBracket(bracketRepository.getOne(Long.valueOf(groupCombo.getSelectionModel().getSelectedItem())));
                    accountRepository.save(account);

                    employee.setPosition(position.getText());
                    employee.setSalary(Double.valueOf(salary.getText()));
                    employeeRepository.save(employee);

                } else {
                    Address address = new Address(name.getText(), surname.getText(), pesel.getText(), city.getText(),
                            street.getText(), Integer.valueOf(houseNumber.getText()), zipCode.getText(), Long.valueOf(telNumber.getText()), eMail.getText());
                    addressRepository.save(address);

                    Account account = new Account();
                    account.setBracket(bracketRepository.getOne(Long.valueOf(groupCombo.getSelectionModel().getSelectedItem())));
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
            } else {
                accountService.fillFieldError();
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
            groupCombo.getItems().clear();
            }
        }
    }

    @FXML
    void listViewClicked(){
        listViewSelectedIndex = listViewPane.getSelectionModel().getSelectedIndex();

    }
    @FXML
    void editClientClicked(){
        if(StoredData.isAdmin()){
            if (!listViewPane.getSelectionModel().isEmpty()) {
                List<Bracket> allBrackets = bracketRepository.findAll();
                for(Bracket br : allBrackets){
                    groupCombo.getItems().add(String.valueOf(br.getId()));
                }
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
                groupCombo.getSelectionModel().select(String.valueOf(employee.getAccount().getBracket().getId()));

                listViewPane.setVisible(false);
                addClientBtn.setVisible(false);

                editClientBtn.setVisible(false);
                deleteClientBtn.setVisible(false);


                addClientPane.setVisible(true);
                edit = true;
            } else {
                accountService.simpleError();
            }

        }else{
            accountService.showError();
        }

    }
    @FXML
    void deleteClientClicked(){

    if(StoredData.isAdmin()){
        if (!listViewPane.getSelectionModel().isEmpty()) {
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
        } else {
            accountService.simpleError();
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


        Label label[] = new Label[employees.size()*6+1];
        HBox hBox[] = new HBox[employees.size()*6+1];
        HBox hBoxBig[] = new HBox[employees.size()+1];
        VBox vBox[] = new VBox[employees.size()+1];
        HBox hBoxForImage[] = new HBox[employees.size()+1];

        Label idLabel[] = new Label[employees.size()+1];
        Label nameLabel[] = new Label[employees.size()+1];
        Label surnameLabel[] = new Label[employees.size()+1];
        Label emailLabel[] = new Label[employees.size()+1];
        Label phoneLabel[] = new Label[employees.size()+1];

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
            phoneLabel[y].setStyle("-fx-font-weight: bold ");

            //creating boxes for labels
            for(int i = 0; i<5;i++) {
                hBox[x+i] = new HBox();
            }

            label[x] = new Label(cli.getId().toString());
            label[x+1] = new Label(cli.getAddress().getName());
            label[x+2] = new Label(cli.getAddress().getSurname());
            label[x+3] = new Label(cli.getAddress().getEmail());
            label[x+4] = new Label(cli.getAddress().getTelephoneNumber().toString());


            hBox[x+1].getChildren().add(nameLabel[y]);
            hBox[x+1].getChildren().add(label[x+1]);
            hBox[x+4].getChildren().add(phoneLabel[y]);
            hBox[x+4].getChildren().add(label[x+4]);
            hBox[x].getChildren().add(idLabel[y]);
            hBox[x].getChildren().add(label[x]);
            hBox[x+2].getChildren().add(surnameLabel[y]);
            hBox[x+2].getChildren().add(label[x+2]);
            hBox[x+3].getChildren().add(emailLabel[y]);
            hBox[x+3].getChildren().add(label[x+3]);

            vBox[y] = new VBox();
            vBox[y].getChildren().add(hBox[x]);
            vBox[y].getChildren().add(hBox[x+1]);
            vBox[y].getChildren().add(hBox[x+2]);
            vBox[y].getChildren().add(hBox[x+3]);
            vBox[y].getChildren().add(hBox[x+4]);

            hBoxBig[z] = new HBox();
            hBoxForImage[y] = new HBox();
            imagePerson[y] = new ImageView();

            imagePerson[y].setImage(image);


            hBoxForImage[y].getChildren().add(imagePerson[y]);
            hBoxForImage[y].getChildren().add(vBox[y]);

            listViewPane.getItems().add(hBoxForImage[y]);

            if(y%2 == 1){


                z++;
            }

            y++;
            x+=5;


        }


    }
}
