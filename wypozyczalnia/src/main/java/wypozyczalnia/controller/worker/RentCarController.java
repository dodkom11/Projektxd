package wypozyczalnia.controller.worker;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import wypozyczalnia.model.Car;
import wypozyczalnia.model.Client;
import wypozyczalnia.model.Rent;
import wypozyczalnia.repository.*;
import wypozyczalnia.service.AccountService;
import wypozyczalnia.utils.SceneManager;
import javafx.scene.input.MouseEvent;
import wypozyczalnia.utils.SceneType;


import java.time.LocalDate;
import java.util.*;

@Controller
public class RentCarController {
    private SceneManager sceneManager;

    private double x = 0;
    private double y = 0;
    private int index;
    private int carIndex;

    List<Car> filteredCars;
    List<Client> clients;


    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CarRepository carRepository;
    @Autowired
    private RentRepository rentRepository;

    @Autowired
    private ClientRepository clientRepository;

    @FXML
    private ComboBox<String> brandComboBox;
    @FXML
    private ComboBox<String> modelComboBox;
    @FXML
    private ComboBox<String> typeComboBox;
    @FXML
    private TextField priceFrom;
    @FXML
    private TextField priceTo;
    @FXML
    private TextField capacityFrom;
    @FXML
    private TextField capacityTo;
    @FXML
    private TextField yearFrom;
    @FXML
    private TextField yearTo;

    @FXML
    private ListView<HBox> carListView;
    @FXML
    private AnchorPane carPane;
    @FXML
    private AnchorPane rentPane;

    @FXML
    private ComboBox<String> clientComboBox;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField rentalTime;
    @FXML
    private Label brandLabel;
    @FXML
    private Label modelLabel;
    @FXML
    private Label priceLabel;
    @FXML
    private Label totalPriceLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private VBox detailsPane;
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
    void searchClicked(){
        List<Car> cars = carRepository.findAll();
        filteredCars = new ArrayList<>();


        float carPriceDown;
        float carPriceUp;
        float carCapacityDown;
        float carCapacityUp;
        int carYearDown;
        int carYearUp;
        boolean model, brand, type;



        if(priceFrom.getText().equals("")){
            carPriceDown = 0f;
        }else{
            carPriceDown = Float.valueOf(priceFrom.getText());
        }
        if(priceTo.getText().equals("")){
            carPriceUp = 5000000f;
        }else{
            carPriceUp = Float.valueOf(priceTo.getText());
        }

        if(capacityFrom.getText().equals("")){
            carCapacityDown = 0f;
        }else{
            carCapacityDown = Float.valueOf(capacityFrom.getText());
        }
        if(capacityTo.getText().equals("")){
            carCapacityUp = 5000000f;
        }else{
            carCapacityUp = Float.valueOf(capacityTo.getText());
        }

        if(yearFrom.getText().equals("")){
            carYearDown = 0;
        }else{
            carYearDown = Integer.valueOf(yearFrom.getText());
        }
        if(yearTo.getText().equals("")){
            carYearUp = 500000;
        }else{
            carYearUp = Integer.valueOf(yearTo.getText());
        }

        for(Car car : cars){

            if((modelComboBox.getSelectionModel().getSelectedItem() == null) || (modelComboBox.getSelectionModel().getSelectedItem().equals(""))){
                model = true;
            }else{
                model = car.getModel().equals(modelComboBox.getSelectionModel().getSelectedItem());
            }

            if((brandComboBox.getSelectionModel().getSelectedItem() == null) || (brandComboBox.getSelectionModel().getSelectedItem().equals(""))){
                brand = true;
            }else{
                brand = car.getBrand().equals(brandComboBox.getSelectionModel().getSelectedItem());
            }

            if((typeComboBox.getSelectionModel().getSelectedItem() == null) || (typeComboBox.getSelectionModel().getSelectedItem().equals(""))){
                type = true;
            }else{
                type = car.getType().equals(typeComboBox.getSelectionModel().getSelectedItem());
            }


            if(     car.getPrice() >= carPriceDown &&
                    car.getPrice() <= carPriceUp &&
                    car.getCapacity() >= carCapacityDown &&
                    car.getCapacity() <= carCapacityUp &&
                    car.getProductionYear() >= carYearDown &&
                    car.getProductionYear() <= carYearUp &&
                    brand && type && model){
                filteredCars.add(car);

            }

        }

        fillCarList();

    }

    @FXML
    void carListClicked(){

    }

    @FXML
    void rentalDetailsClicked(){
        LocalDate localDate = datePicker.getValue();
        dateLabel.setText(localDate.toString());
        float totalPrice = Float.valueOf(priceLabel.getText()) * Float.valueOf(rentalTime.getText());
        totalPriceLabel.setText(String.valueOf(totalPrice));
        timeLabel.setText(rentalTime.getText());
        detailsPane.setVisible(true);
    }
    @FXML
    void confirmRentalClicked(){
        LocalDate localDate = datePicker.getValue();

        Calendar c1 = Calendar.getInstance();
        c1.set(Calendar.MONTH, localDate.getMonth().getValue());
        c1.set(Calendar.DATE, localDate.getDayOfMonth());
        c1.set(Calendar.YEAR, localDate.getYear());

        Date date = c1.getTime();

        Rent rent = new Rent();
        rent.setCar(filteredCars.get(carIndex));
        rent.setClient(clients.get(clientComboBox.getSelectionModel().getSelectedIndex()));
        rent.setEmployee(accountRepository.findById(StoredData.getLoggedUserId()).getEmployee());
        rent.setRentalDate(date);
        rent.setRentalTime(Integer.valueOf(rentalTime.getText()));
        rentRepository.save(rent);

        detailsPane.setVisible(false);
        rentPane.setVisible(false);
        carPane.setVisible(true);

        datePicker.setValue(null);
        clientComboBox.getItems().clear();
        rentalTime.clear();

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
    void clientsClicked(){
        sceneManager.show(SceneType.WORKER_OUR_CARS);
    }
    @FXML
    void ourCarsClicked(){ sceneManager.show(SceneType.WORKER_OUR_CARS); }

    @FXML
    public void initialize() {
        if(StoredData.isAdmin()){
            panelLabel.setText("Admin Panel");
        }else{
            panelLabel.setText("Worker Panel");
        }
        mainTitleLabel.setText("Rent a Car");

        typeComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {
                System.out.println(ov);
                System.out.println(t);
                System.out.println(t1);
                setBrands();
            }
        });

        brandComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {
                setModels();
            }
        });


        setTypes();

    }

    private void setTypes(){
        typeComboBox.getItems().clear();
        typeComboBox.getItems().add("");

        List<Car> cars = carRepository.findAll();

        Set<String> uniqueCars = new HashSet<>();

        for(Car car : cars){
            uniqueCars.add(car.getType());
        }
        for(String type : uniqueCars){
            typeComboBox.getItems().add(type);
        }
    }

    private void setBrands(){
        brandComboBox.getItems().clear();
        brandComboBox.getItems().add("");
        String type = typeComboBox.getSelectionModel().getSelectedItem();
        List<Car> cars = carRepository.findAllByType(type);
        Set<String> uniqueCars = new HashSet<>();

        for(Car car : cars){
            uniqueCars.add(car.getBrand());
        }
        for(String brand : uniqueCars){
            brandComboBox.getItems().add(brand);
        }
    }

    private void setModels(){
        modelComboBox.getItems().clear();
        modelComboBox.getItems().add("");
        String brand = brandComboBox.getSelectionModel().getSelectedItem();
        List<Car> cars = carRepository.findAllByBrand(brand);

        Set<String> uniqueCars = new HashSet<>();

        for(Car car : cars){
            uniqueCars.add(car.getModel());
        }
        for(String model : uniqueCars){
            modelComboBox.getItems().add(model);
        }
    }




    private void fillCarList(){
        carListView.getItems().clear();
        // create hbox
        HBox hBox[] = new HBox[filteredCars.size()];
        // create image and vbox
        ImageView carImage[] = new ImageView[filteredCars.size()];
        Button btnRent[] = new Button[filteredCars.size()];
        VBox vBox[] = new VBox[filteredCars.size()];
        Label label[] = new Label[filteredCars.size()*7];

        Image image = new Image("/icons/car.png");

        int x = 0;
        for(Car car : filteredCars) {
            hBox[x] = new HBox();
            carImage[x] = new ImageView();
            vBox[x] = new VBox();
            btnRent[x] = new Button("Rent this car");
            index = x;
            btnRent[x].setOnMouseClicked(event -> rentPanel(index));


            label[x*7] = new Label("  ID: " + car.getId());
            label[x*7 + 1] = new Label("  Brand: " + car.getBrand());
            label[x*7 + 2] = new Label("  Model: " + car.getModel());
            label[x*7 + 3] = new Label("  Year: " + car.getProductionYear());
            label[x*7 + 4] = new Label("  Capacity: " + car.getCapacity());
            label[x*7+ 5] = new Label("  Type: " + car.getType());
            label[x*7 + 6] = new Label("  Price/day: " + car.getPrice());


            // fill image and vbox
            carImage[x].setImage(image);

            for(int i = 0; i < 7; i++){
                vBox[x].getChildren().add(label[x*7+i]);
            }

            // add image and vbox to hbox
            hBox[x].getChildren().add(carImage[x]);
            hBox[x].getChildren().add(vBox[x]);
            hBox[x].getChildren().add(btnRent[x]);

            // add hbox to listview
            carListView.getItems().add(hBox[x]);

            x++;
        }

    }

    private void rentPanel(int index){
        carIndex = index;

        carPane.setVisible(false);
        rentPane.setVisible(true);

        clients = clientRepository.findAll();

        for(Client client : clients) {
            clientComboBox.getItems().add(client.getAddress().getName() + " " + client.getAddress().getSurname());
        }
        brandLabel.setText(filteredCars.get(index).getBrand());
        modelLabel.setText(filteredCars.get(index).getModel());
        priceLabel.setText(String.valueOf(filteredCars.get(index).getPrice()));

    }

    @Autowired
    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

}
























