package wypozyczalnia.controller.worker;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import wypozyczalnia.model.Car;
import wypozyczalnia.model.Employee;
import wypozyczalnia.model.Rent;
import wypozyczalnia.repository.CarRepository;
import wypozyczalnia.repository.EmployeeRepository;
import wypozyczalnia.repository.RentRepository;
import wypozyczalnia.utils.SceneManager;
import wypozyczalnia.utils.SceneType;

import java.util.List;

@Controller
public class DiagramsController {
    SceneManager sceneManager;

    private double x = 0;
    private double y = 0;

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private RentRepository rentRepository;
    @Autowired
    private CarRepository carRepository;

    @FXML
    private ListView<Pane> chartListView;

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
    void logoutClicked(){
        sceneManager.show(SceneType.MAIN);
    }
    @FXML
    void tasksClicked(){
        sceneManager.show(SceneType.WORKER_MAIN);
    }
    @FXML
    void clientsClicked(){
        sceneManager.show(SceneType.WORKER_CLIENT_MANAGEMENET);
    }
    @FXML
    void rentClicked(){ sceneManager.show(SceneType.WORKER_RENT_CAR); }
    @FXML
    void ourCarsClicked() { sceneManager.show(SceneType.WORKER_OUR_CARS); }

    @FXML
    public void initialize() {
        Pane pane = new Pane();
        pane.getChildren().add(employeChart());
        Pane pane2 = new Pane();
        pane2.getChildren().add(rentChart());
        chartListView.getItems().add(pane);
        chartListView.getItems().add(pane2);

    }

    @Autowired
    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    private LineChart createChart(){
        //Defining X axis
        NumberAxis xAxis = new NumberAxis(1960, 2020, 10);
        xAxis.setLabel("Years");

        //Defining y axis
        NumberAxis yAxis = new NumberAxis(0, 350, 50);
        yAxis.setLabel("No.of schools");

        LineChart linechart = new LineChart(xAxis, yAxis);

        XYChart.Series series = new XYChart.Series();
        series.setName("No of schools in an year");

        series.getData().add(new XYChart.Data(1970, 15));
        series.getData().add(new XYChart.Data(1980, 30));
        series.getData().add(new XYChart.Data(1990, 60));
        series.getData().add(new XYChart.Data(2000, 120));
        series.getData().add(new XYChart.Data(2013, 240));
        series.getData().add(new XYChart.Data(2014, 300));

        //Setting the data to Line chart
        linechart.getData().add(series);

        return linechart;
    }

    private BarChart employeChart(){

        List<Employee> employees = employeeRepository.findAll();
        //Defining X axis
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Employee");

        //Defining y axis
        NumberAxis yAxis = new NumberAxis(0, 5000, 500);
        yAxis.setLabel("Earnings");

        BarChart barChart = new BarChart(xAxis, yAxis);

        XYChart.Series series = new XYChart.Series();
        series.setName("Salary per employee");

        for(Employee emp : employees){
            String name = emp.getAddress().getName();
            String surname = emp.getAddress().getSurname();
            double earnings = emp.getSalary();
            series.getData().add(new XYChart.Data(name + " " + surname, earnings));
        }


        //Setting the data to Line chart
        barChart.getData().add(series);

        return barChart;
    }

    private BarChart rentChart(){

        List<Car> cars = carRepository.findAll();


        //Defining X axis
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Cars");

        //Defining y axis
        NumberAxis yAxis = new NumberAxis(0, 50, 5);
        yAxis.setLabel("Rents");

        BarChart barChart = new BarChart(xAxis, yAxis);

        XYChart.Series series = new XYChart.Series();
        series.setName("Rents per car");

        for(Car car : cars){
            List<Rent> rent = rentRepository.findAllByCar(car);
            series.getData().add(new XYChart.Data(car.getBrand() + " " + car.getModel(), rent.size()));
        }



        //Setting the data to Line chart
        barChart.getData().add(series);

        return barChart;
    }
}