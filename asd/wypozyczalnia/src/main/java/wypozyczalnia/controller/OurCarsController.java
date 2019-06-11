package wypozyczalnia.controller;

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
import wypozyczalnia.model.Car;
import wypozyczalnia.repository.CarRepository;
import wypozyczalnia.service.AccountService;
import wypozyczalnia.utils.SceneManager;
import javafx.scene.input.MouseEvent;
import wypozyczalnia.utils.SceneType;

import java.util.List;

@Controller
public class OurCarsController {
    private SceneManager sceneManager;

    private double x = 0;
    private double y = 0;
    private int listViewSelectedIndex;
    private boolean edit = false;

    @Autowired
    private AccountService accountService;
    @Autowired
    private CarRepository carRepository;


    List<Car> cars;

    Car car;

    @FXML
    private Pane addCarPane;
    @FXML
    private TextField brand;
    @FXML
    private TextField model;
    @FXML
    private TextField productionYear;
    @FXML
    private TextField vin;
    @FXML
    private TextField capacity;
    @FXML
    private TextField type;
    @FXML
    private TextField price;

    @FXML
    private ListView<HBox> listViewPane;
    @FXML
    private Button editCarBtn;
    @FXML
    private Button deleteCarBtn;
    @FXML
    private Button addCarBtn;
    @FXML
    private Label panelLabel;
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
    void clientsClicked(){
        sceneManager.show(SceneType.WORKER_CLIENT_MANAGEMENT);
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
        fillClientData();
    }

    @FXML
    void addCarClicked(){
        listViewPane.setVisible(false);
        addCarBtn.setVisible(false);
        editCarBtn.setVisible(false);
        deleteCarBtn.setVisible(false);

        addCarPane.setVisible(true);
    }


    @FXML
    void confirmAddClicked(){
        Car newCar;
        if(edit) {
            newCar = carRepository.getOne(car.getId());

        }else {
            newCar = new Car();
        }

        newCar.setBrand(brand.getText());
        newCar.setCapacity(Float.valueOf(capacity.getText()));
        newCar.setModel(model.getText());
        newCar.setPrice(Float.valueOf(price.getText()));
        newCar.setProductionYear(Integer.valueOf(productionYear.getText()));
        newCar.setType(type.getText());
        newCar.setVin(vin.getText());
        carRepository.save(newCar);

        addCarPane.setVisible(false);

        fillClientData();
        edit = false;

        brand.clear();
        model.clear();
        productionYear.clear();
        vin.clear();
        capacity.clear();
        type.clear();
        price.clear();

        listViewPane.setVisible(true);
        addCarBtn.setVisible(true);
        editCarBtn.setVisible(true);
        deleteCarBtn.setVisible(true);
    }

    @FXML
    void listViewClicked(){
        listViewSelectedIndex = listViewPane.getSelectionModel().getSelectedIndex();

    }

    /**
     * getting data from database to assign value to TextField
     */
    @FXML
    void editCarClicked(){
        if(StoredData.isAdmin()) {
            car = carRepository.getOne(cars.get(listViewSelectedIndex).getId());

            brand.setText(car.getBrand());
            model.setText(car.getModel());
            productionYear.setText(String.valueOf(car.getProductionYear()));
            vin.setText(car.getVin());
            capacity.setText(String.valueOf(car.getCapacity()));
            type.setText(car.getType());
            price.setText(String.valueOf(car.getPrice()));

            listViewPane.setVisible(false);
            addCarBtn.setVisible(false);
            editCarBtn.setVisible(false);
            deleteCarBtn.setVisible(false);

            addCarPane.setVisible(true);

            edit = true;
        }else{
            accountService.showError();
        }

    }

    /**
     * deleting car, checking if we are logged as admin
     */
    @FXML
    void deleteCarClicked(){

        if(StoredData.isAdmin()){

            Car deleteCar = carRepository.getOne(cars.get(listViewSelectedIndex).getId());

            carRepository.delete(deleteCar);
            fillClientData();
        }else{
            accountService.showError();
        }
    }

    @Autowired
    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    /**
     * filling ListView with cars
     */
    private void fillClientData(){
        listViewPane.getItems().clear();

        cars = carRepository.findAll();

        Label label[] = new Label[cars.size()*6];
        HBox hBox[] = new HBox[cars.size()*6];
        HBox hBoxBig[] = new HBox[cars.size()];
        VBox vBox[] = new VBox[cars.size()];
        HBox hBoxForImage[] = new HBox[cars.size()];

        Label idLabel[] = new Label[cars.size()];
        Label brandLabel[] = new Label[cars.size()];
        Label modelLabel[] = new Label[cars.size()];
        Label capacityLabel[] = new Label[cars.size()];
        Label priceLabel[] = new Label[cars.size()];

        ImageView imagePerson[] = new ImageView[cars.size()];
        Image image = new Image("/icons/icons8-car-100.png");

        int x = 0;
        int y = 0;
        int z = 0;


        for(Car cli : cars){

            idLabel[y] = new Label("ID: ");
            idLabel[y].setStyle("-fx-font-weight: bold");
            brandLabel[y] = new Label("Brand: ");
            brandLabel[y].setStyle("-fx-font-weight: bold");
            modelLabel[y] = new Label("Model: ");
            modelLabel[y].setStyle("-fx-font-weight: bold");
            capacityLabel[y] = new Label("Capacity: ");
            capacityLabel[y].setStyle("-fx-font-weight: bold");
            priceLabel[y] = new Label("Price: ");
            priceLabel[y].setStyle("-fx-font-weight: bold");

            hBox[x] = new HBox();
            hBox[x+1] = new HBox();
            hBox[x+2] = new HBox();
            hBox[x+3] = new HBox();
            hBox[x+4] = new HBox();

            label[x] = new Label(cli.getId().toString());
            label[x+1] = new Label(cli.getBrand());
            label[x+2] = new Label(cli.getModel());
            label[x+3] = new Label(String.valueOf(cli.getCapacity()));
            label[x+4] = new Label(String.valueOf(cli.getPrice()));

            hBox[x].getChildren().add(idLabel[y]);
            hBox[x].getChildren().add(label[x]);
            hBox[x+1].getChildren().add(brandLabel[y]);
            hBox[x+1].getChildren().add(label[x+1]);
            hBox[x+2].getChildren().add(modelLabel[y]);
            hBox[x+2].getChildren().add(label[x+2]);
            hBox[x+3].getChildren().add(capacityLabel[y]);
            hBox[x+3].getChildren().add(label[x+3]);
            hBox[x+4].getChildren().add(priceLabel[y]);
            hBox[x+4].getChildren().add(label[x+4]);

            vBox[y] = new VBox();

            for(int i = 0; i < 5 ;i++){
                vBox[y].getChildren().add(hBox[x+i]);
            }

            imagePerson[y] = new ImageView();
            hBoxForImage[y] = new HBox();

            hBoxForImage[y].getChildren().add(vBox[y]);
            imagePerson[y].setImage(image);
            hBoxForImage[y].getChildren().add(imagePerson[y]);

            listViewPane.getItems().add(hBoxForImage[y]);

            if(y%2 == 1){
                z++;
            }

            y++;
            x+=5;

        }

    }
}
