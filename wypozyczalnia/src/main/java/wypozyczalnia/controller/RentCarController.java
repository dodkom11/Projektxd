package wypozyczalnia.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import wypozyczalnia.config.DisabledRange;
import wypozyczalnia.config.StoredData;
import wypozyczalnia.model.*;
import wypozyczalnia.repository.*;
import wypozyczalnia.service.AccountService;
import wypozyczalnia.utils.SceneManager;
import javafx.scene.input.MouseEvent;
import wypozyczalnia.utils.SceneType;


import java.time.*;
import java.util.*;

@Controller
public class RentCarController{
    private SceneManager sceneManager;

    private double x = 0;
    private double y = 0;
    private Car correctCar;

    List<Car> filteredCars;
    List<Client> clients;
    List<LocalDate> disableDates;


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
    private Label errorLabel;
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


    /**
     * search button, which filter our data depending what we choose
     */
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
        errorLabel.setVisible(false);

        boolean badTime = false;

        LocalDate localDate = datePicker.getValue();
        LocalDate checkDate;



        for(LocalDate ld : disableDates){
            for(int i = 0; i < (Integer.valueOf(rentalTime.getText())-1); i++){
                checkDate = localDate.plusDays(Integer.valueOf(rentalTime.getText())-1-i);
                if( checkDate.getYear() == ld.getYear() &&
                        checkDate.getMonth().getValue() == ld.getMonth().getValue() &&
                        checkDate.getDayOfMonth() == ld.getDayOfMonth()){
                    badTime = true;
                }
            }

        }

        if(!badTime){
            dateLabel.setText(localDate.toString());
            float totalPrice = Float.valueOf(priceLabel.getText()) * Float.valueOf(rentalTime.getText());
            totalPriceLabel.setText(String.valueOf(totalPrice));
            timeLabel.setText(rentalTime.getText());
            detailsPane.setVisible(true);
        }else{
            errorLabel.setVisible(true);

        }
    }
    @FXML
    void confirmRentalClicked(){
        LocalDate localDate = datePicker.getValue();



        Date date = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

        Rent rent = new Rent();
        rent.setCar(correctCar);
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
        if(StoredData.isAdmin()){
            sceneManager.show(SceneType.ADMIN_GROUPS);
        }else{

            sceneManager.show(SceneType.WORKER_MAIN);
        }
    }
    @FXML
    void clientsClicked(){
        sceneManager.show(SceneType.WORKER_OUR_CARS);
    }
    @FXML
    void employeesBtnClicked(){sceneManager.show(SceneType.ADMIN_EMPLOYEE_MANAGEMENT);}
    @FXML
    void ourCarsClicked(){ sceneManager.show(SceneType.WORKER_OUR_CARS); }
    @FXML
    void diagramsClicked(){
        sceneManager.show(SceneType.WORKER_DIAGRAMS);
    }

    @FXML
    public void initialize() {
        if(StoredData.isAdmin()){
            panelLabel.setText("Admin Panel");
            employeesBtn.setVisible(true);
            groupTask.setText("Groups");
        }else{
            panelLabel.setText("Worker Panel");
            employeesBtn.setVisible(false);
            groupTask.setText("Tasks");
        }

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
        HBox hBox[] = new HBox[filteredCars.size()];
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

            btnRent[x].setOnMouseClicked(event -> rentPanel(car));


            label[x*7] = new Label("  ID: " + car.getId());
            label[x*7 + 1] = new Label("  Brand: " + car.getBrand());
            label[x*7 + 2] = new Label("  Model: " + car.getModel());
            label[x*7 + 3] = new Label("  Year: " + car.getProductionYear());
            label[x*7 + 4] = new Label("  Capacity: " + car.getCapacity());
            label[x*7+ 5] = new Label("  Type: " + car.getType());
            label[x*7 + 6] = new Label("  Price/day: " + car.getPrice());


            carImage[x].setImage(image);

            for(int i = 0; i < 7; i++){
                vBox[x].getChildren().add(label[x*7+i]);
            }

            hBox[x].getChildren().add(carImage[x]);
            hBox[x].getChildren().add(vBox[x]);
            hBox[x].getChildren().add(btnRent[x]);

            carListView.getItems().add(hBox[x]);

            x++;
        }

    }

    private void rentPanel(Car car){
        correctCar = car;

        carPane.setVisible(false);
        rentPane.setVisible(true);

        clients = clientRepository.findAll();

        for(Client client : clients) {
            clientComboBox.getItems().add(client.getAddress().getName() + " " + client.getAddress().getSurname());
        }
        brandLabel.setText(car.getBrand());
        modelLabel.setText(car.getModel());
        priceLabel.setText(String.valueOf(car.getPrice()));

        dataPickerDisable(car);
        System.out.println(car.getBrand() + " " + car.getModel());

    }


    private void dataPickerDisable(Car car){
        List<Rent> rents = rentRepository.findAllByCar(car);
        disableDates = new ArrayList<>();
        System.out.println(car.getBrand() + " " + car.getModel());

        for(Rent rent : rents){
            for(int i = 0; i < rent.getRentalTime();i++){
                System.out.println(rent.getRentalDate() + "  " + rent.getRentalTime());
                LocalDate ld = Instant.ofEpochMilli(rent.getRentalDate().getTime())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();

                disableDates.add(ld.plusDays(i));
            }
        }



        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate date, boolean empty) {
                        super.updateItem(date, empty);
                        for(LocalDate local : disableDates){

                            if(date.getYear() == local.getYear() &&
                               date.getMonth().getValue() == local.getMonth().getValue() &&
                               date.getDayOfMonth() == local.getDayOfMonth()
                            ) {
                                setDisable(true);
                                setStyle("-fx-background-color: #ffc0cb;");

                            }
                        }
                    }
                };
            }
        };
        datePicker.setDayCellFactory(dayCellFactory);
    }

    @Autowired
    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

}
























