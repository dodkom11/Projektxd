package wypozyczalnia.controller.worker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import wypozyczalnia.config.StoredData;
import wypozyczalnia.model.Address;
import wypozyczalnia.model.Client;
import wypozyczalnia.repository.AccountRepository;
import wypozyczalnia.repository.AddressRepository;
import wypozyczalnia.repository.CarRepository;
import wypozyczalnia.repository.ClientRepository;
import wypozyczalnia.service.AccountService;
import wypozyczalnia.utils.SceneManager;
import javafx.scene.input.MouseEvent;
import wypozyczalnia.utils.SceneType;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ClientManagementController {
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
    private CarRepository carRepository;

    private GridPane gridPane;

    List<Client> clients;

    Client client;

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
        sceneManager.show(SceneType.WORKER_MAIN);
    }
    @FXML
    void ourCarsClicked(){
        sceneManager.show(SceneType.WORKER_OUR_CARS);
    }
    @FXML
    void rentClicked(){ sceneManager.show(SceneType.WORKER_RENT_CAR); }


    @FXML
    public void initialize() {
        if(StoredData.isAdmin()){
            panelLabel.setText("Admin Panel");
        }else{
            panelLabel.setText("Worker Panel");
        }
        mainTitleLabel.setText("Clients");

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
            Address address = addressRepository.getOne(client.getAddress().getId());
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
        }else{
            Address address = new Address(name.getText(), surname.getText(), pesel.getText(), city.getText(),
                    street.getText(), Integer.valueOf(houseNumber.getText()), zipCode.getText(), Long.valueOf(telNumber.getText()), eMail.getText());
            addressRepository.save(address);
            Client client = new Client(address);
            clientRepository.save(client);
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


    }

    @FXML
    void listViewClicked(){
        listViewSelectedIndex = listViewPane.getSelectionModel().getSelectedIndex();

    }
    @FXML
    void editClientClicked(){
        if(StoredData.isAdmin()){
            client = clientRepository.getOne(clients.get(listViewSelectedIndex).getId());

            name.setText(client.getAddress().getName());
            surname.setText(client.getAddress().getSurname());
            eMail.setText(client.getAddress().getEmail());
            city.setText(client.getAddress().getCity());
            pesel.setText(client.getAddress().getPesel());
            houseNumber.setText(String.valueOf(client.getAddress().getHouseNumber()));
            zipCode.setText(client.getAddress().getZipCode());
            telNumber.setText(String.valueOf(client.getAddress().getTelephoneNumber()));
            street.setText(client.getAddress().getStreet());

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

            Client deleteClient = clientRepository.getOne(clients.get(listViewSelectedIndex).getId());

            deleteClient.setActive(false);
            clientRepository.save(deleteClient);
            fillClientData();
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
        gridPane = new GridPane();
        gridPane.prefWidthProperty().bind(clientPane.widthProperty());
        gridPane.prefHeightProperty().bind(clientPane.heightProperty());
        ColumnConstraints cc = new ColumnConstraints(100, 100, Double.MAX_VALUE,
                Priority.ALWAYS, HPos.CENTER, true);
        gridPane.getColumnConstraints().addAll(cc, cc);

        RowConstraints rc = new RowConstraints(20, 20, Double.MAX_VALUE,
                Priority.ALWAYS, VPos.CENTER, true);
        gridPane.getRowConstraints().addAll(rc, rc);

        gridPane.setStyle("-fx-padding: 20");

        //gridPane.add(new Button("Press me!"), 1, 0); // column=1 row=0

        clients = clientRepository.findAll();


        Label label[] = new Label[clients.size()*6];
        HBox hBox[] = new HBox[clients.size()*6];
        HBox hBoxBig[] = new HBox[clients.size()];
        VBox vBox[] = new VBox[clients.size()];
        HBox hBoxForImage[] = new HBox[clients.size()];

        Label idLabel[] = new Label[clients.size()];
        Label nameLabel[] = new Label[clients.size()];
        Label surnameLabel[] = new Label[clients.size()];
        Label emailLabel[] = new Label[clients.size()];
        Label phoneLabel[] = new Label[clients.size()];

        ImageView imagePerson[] = new ImageView[clients.size()];
        Image image = new Image("/icons/icons8-customer-100.png");

        List<Client> ccc = new ArrayList<>();

        for(Client cli : clients) {
            if(cli.getActive()){
                ccc.add(cli);
            }
        }
        clients.clear();
        clients.addAll(ccc);
        int x = 0;
        int y = 0;
        int z = 0;




       for(Client cli : ccc){

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

            //gridPane.add(hBoxForImage[y], y%2 , z);
            if(y%2 == 1){


                z++;
            }

            y++;
            x+=5;


        }


       // listViewPane.getItems().add(hBoxBig);

    }
}
