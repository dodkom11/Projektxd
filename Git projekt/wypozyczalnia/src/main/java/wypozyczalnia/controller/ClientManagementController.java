package wypozyczalnia.controller;

import javafx.beans.binding.BooleanBinding;
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
import wypozyczalnia.model.Address;
import wypozyczalnia.model.Client;
import wypozyczalnia.repository.AddressRepository;
import wypozyczalnia.repository.ClientRepository;
import wypozyczalnia.service.AccountService;
import wypozyczalnia.utils.SceneManager;
import javafx.scene.input.MouseEvent;
import wypozyczalnia.utils.SceneType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private AddressRepository addressRepository;
    @Autowired
    private ClientRepository clientRepository;

    List<Client> clients;

    Client client;

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
    private Button confirmBtn;
    @FXML
    private Label labelError;

    @FXML
    void dragged(MouseEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();

        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);
        stage.setOpacity(0.5);
    }

    @FXML
    void released(MouseEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();

        stage.setOpacity(1);
    }

    @FXML
    void pressed(MouseEvent event) {
        x = event.getSceneX();
        y = event.getSceneY();
    }

    @FXML
    void closeBtnClicked(MouseEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    @FXML
    void minimizeBtnClicked(MouseEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    void logoutClicked() {
        sceneManager.show(SceneType.MAIN);
    }

    @FXML
    void employeesBtnClicked() {
        sceneManager.show(SceneType.ADMIN_EMPLOYEE_MANAGEMENT);
    }

    @FXML
    void tasksClicked() {
        if (StoredData.isAdmin()) {
            sceneManager.show(SceneType.ADMIN_GROUPS);
        } else {

            sceneManager.show(SceneType.WORKER_MAIN);
        }
    }

    @FXML
    void ourCarsClicked() {
        sceneManager.show(SceneType.WORKER_OUR_CARS);
    }

    @FXML
    void rentClicked() {
        sceneManager.show(SceneType.WORKER_RENT_CAR);
    }

    @FXML
    void diagramsClicked() {
        sceneManager.show(SceneType.WORKER_DIAGRAMS);
    }


    @FXML
    public void initialize() {
        if (StoredData.isAdmin()) {
            employeesBtn.setVisible(true);
            groupTask.setText("Groups");
            panelLabel.setText("Admin Panel");
        } else {
            employeesBtn.setVisible(false);
            groupTask.setText("Tasks");
            panelLabel.setText("Worker Panel");
            deleteClientBtn.setVisible(false);
            editClientBtn.setVisible(false);
        }

        zipCode.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*(\\-\\d*)?")) {
                    zipCode.setText(oldValue);
                }
            }
        });

        // Bit of a hack. Probably need a ValidationBinding extends BooleanBinding with a message property:
        confirmBtn.disableProperty()
                .bind(name.textProperty().isEmpty()
                .or(surname.textProperty().isEmpty())
                .or(pesel.textProperty().isEmpty())
                .or(city.textProperty().isEmpty())
                .or(street.textProperty().isEmpty())
                .or(houseNumber.textProperty().isEmpty())
                .or(zipCode.textProperty().isEmpty())
                .or(telNumber.textProperty().isEmpty())
                .or(eMail.textProperty().isEmpty())
                );

        fillClientData();
    }

    /**
     * open client panel
     */
    @FXML
    void addClientClicked() {

        listViewPane.setVisible(false);
        addClientBtn.setVisible(false);
        editClientBtn.setVisible(false);
        deleteClientBtn.setVisible(false);

        addClientPane.setVisible(true);
    }

    private boolean canEdit(List<TextField> textField) {
        boolean canEdit = true;
        for (TextField tx : textField) {
            if (tx.getText().equals("")) {
                canEdit = false;
            }
        }
        return canEdit;
    }

    /**
     * confirming the edition of client, checking for errors
     */
    @FXML
    void confirmAddClicked() {
        List<TextField> tx = new ArrayList<>();
        tx.add(name);
        tx.add(surname);
        tx.add(pesel);
        tx.add(city);
        tx.add(street);
        tx.add(houseNumber);
        tx.add(zipCode);
        tx.add(telNumber);
        tx.add(eMail);

                if(pesel.getText().length() == 11){
                    labelError.setVisible(false);
                    if (canEdit(tx)) {

                        try {
                            if (edit) {
                                Address address = addressRepository.getOne(client.getAddress().getId());
                                address.setName(name.getText() + "");
                                address.setSurname(surname.getText());
                                address.setPesel(pesel.getText());
                                address.setCity(city.getText() + "");
                                address.setStreet(street.getText());
                                address.setHouseNumber(Integer.valueOf(houseNumber.getText()));
                                address.setZipCode(zipCode.getText() + "");
                                address.setTelephoneNumber(Long.valueOf(telNumber.getText()));
                                address.setEmail(eMail.getText() + "");
                                addressRepository.save(address);
                            } else {
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

                            name.clear();
                            surname.clear();
                            eMail.clear();
                            city.clear();

                            editClientBtn.setVisible(true);
                            deleteClientBtn.setVisible(true);

                            pesel.clear();
                            houseNumber.clear();
                            zipCode.clear();
                            telNumber.clear();
                            street.clear();

                            edit = false;

                        } catch (Exception e) {
                            accountService.fillFieldError();
                        }
                    } else {
                        accountService.fillFieldError();
                    }
                }else{
                    labelError.setVisible(true);
                    labelError.setText("pesel is incorrect");
                }


    }

    /**
     * saving selected index of ListView where clients are
     */
    @FXML
    void listViewClicked() {
        listViewSelectedIndex = listViewPane.getSelectionModel().getSelectedIndex();

    }

    /**
     * assigning the values from database to TextField,
     * to improve quality of life when editing client
     */
    @FXML
    void editClientClicked() {
        if (StoredData.isAdmin()) {

            if (!listViewPane.getSelectionModel().isEmpty()) {
                client = clientRepository.getOne(clients.get(listViewSelectedIndex).getId());

                name.setText(client.getAddress().getName() + "");
                surname.setText(client.getAddress().getSurname());
                eMail.setText(client.getAddress().getEmail() + "");
                city.setText(client.getAddress().getCity());
                pesel.setText(client.getAddress().getPesel() + "");
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
            } else {
                accountService.simpleError();
            }

        } else {
            accountService.showError();
        }

    }

    /**
     * deleting selected client, checking for permissions
     * only admin can delete
     */
    @FXML
    void deleteClientClicked() {

        if (StoredData.isAdmin()) {
            if (!listViewPane.getSelectionModel().isEmpty()) {

                Client deleteClient = clientRepository.getOne(clients.get(listViewSelectedIndex).getId());
                Address address = addressRepository.getOne(deleteClient.getAddress().getId());

                clientRepository.delete(deleteClient);
                addressRepository.delete(address);
                fillClientData();
            } else {
                accountService.simpleError();
            }

        } else {
            accountService.showError();
        }
    }

    @Autowired
    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    /**
     * fill ListView with cars
     */
    private void fillClientData() {

        clients = clientRepository.findAll();
        listViewPane.getItems().clear();
        int size = clients.size();

        HBox hBoxForImage[] = new HBox[clients.size()];
        Label label[] = new Label[clients.size() * 6];
        HBox hBox[] = new HBox[clients.size() * 6];
        HBox hBoxBig[] = new HBox[clients.size()];
        VBox vBox[] = new VBox[clients.size()];


        Label surnameLabel[] = new Label[size];
        Label emailLabel[] = new Label[size];
        Label phoneLabel[] = new Label[size];
        Label idLabel[] = new Label[size];
        Label nameLabel[] = new Label[size];

        ImageView imagePerson[] = new ImageView[clients.size()];
        Image image = new Image("/icons/icons8-customer-100.png");

        int x = 0;
        int y = 0;
        int z = 0;


        for (Client cli : clients) {

            surnameLabel[y] = new Label("Surname:  ");
            surnameLabel[y].setStyle("-fx-font-weight: bold");
            emailLabel[y] = new Label("E-Mail:  ");
            emailLabel[y].setStyle("-fx-font-weight: bold");
            phoneLabel[y] = new Label("Tel-Number:  ");
            phoneLabel[y].setStyle("-fx-font-weight: bold");

            idLabel[y] = new Label("ID: ");
            idLabel[y].setStyle("-fx-font-weight: bold");
            nameLabel[y] = new Label("Name: ");
            nameLabel[y].setStyle("-fx-font-weight: bold");
            for (int i = 0; i < 5; i++) {
                hBox[x + i] = new HBox();
            }


            label[x + 3] = new Label(cli.getAddress().getEmail());
            label[x + 4] = new Label(cli.getAddress().getTelephoneNumber().toString());
            label[x] = new Label(cli.getId().toString());
            label[x + 1] = new Label(cli.getAddress().getName());
            label[x + 2] = new Label(cli.getAddress().getSurname());

            hBox[x + 3].getChildren().add(emailLabel[y]);
            hBox[x + 3].getChildren().add(label[x + 3]);
            hBox[x + 2].getChildren().add(surnameLabel[y]);
            hBox[x + 2].getChildren().add(label[x + 2]);
            hBox[x + 4].getChildren().add(phoneLabel[y]);
            hBox[x + 4].getChildren().add(label[x + 4]);
            hBox[x].getChildren().add(idLabel[y]);
            hBox[x].getChildren().add(label[x]);
            hBox[x + 1].getChildren().add(nameLabel[y]);
            hBox[x + 1].getChildren().add(label[x + 1]);


            vBox[y] = new VBox();

            vBox[y].getChildren().add(hBox[x + 1]);
            vBox[y].getChildren().add(hBox[x + 2]);
            vBox[y].getChildren().add(hBox[x + 3]);
            vBox[y].getChildren().add(hBox[x + 4]);
            vBox[y].getChildren().add(hBox[x]);

            hBoxBig[z] = new HBox();
            hBoxForImage[y] = new HBox();
            imagePerson[y] = new ImageView();

            imagePerson[y].setImage(image);


            hBoxForImage[y].getChildren().add(imagePerson[y]);
            hBoxForImage[y + 0].getChildren().add(vBox[y]);

            hBoxBig[z].getChildren().add(hBoxForImage[y]);
            listViewPane.getItems().add(hBoxForImage[y]);

            if (y % 2 == 1) {
                z++;
            }
            y++;
            x += 5;
        }
    }
}
