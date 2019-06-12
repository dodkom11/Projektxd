package wypozyczalnia.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import wypozyczalnia.config.StoredData;
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
    @FXML
    private Label groupTask;
    @FXML
    private Label panelLabel;

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private RentRepository rentRepository;
    @Autowired
    private CarRepository carRepository;

    @FXML
    private ListView<Pane> chartListView;
    @FXML
    private HBox employeesBtn;

    @FXML
    void dragged(MouseEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();

        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);
        stage.setOpacity(0.5);
    }

    @FXML
    void pressed(MouseEvent event) {
        x = event.getSceneX();
        y = event.getSceneY();

    }

    @FXML
    void released(MouseEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.setOpacity(1);
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
    void clientsClicked() {
        sceneManager.show(SceneType.WORKER_CLIENT_MANAGEMENT);
    }

    @FXML
    void rentClicked() {
        sceneManager.show(SceneType.WORKER_RENT_CAR);
    }

    @FXML
    void ourCarsClicked() {
        sceneManager.show(SceneType.WORKER_OUR_CARS);
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
        }

        Pane pane = new Pane();
        pane.getChildren().add(employeeChart());
        Pane pane2 = new Pane();
        pane2.getChildren().add(rentChart());
        chartListView.getItems().add(pane);
        chartListView.getItems().add(pane2);

    }

    @Autowired
    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    /**
     *
     * @return chart that show salary per employee
     */
    private BarChart employeeChart() {

        List<Employee> employees = employeeRepository.findAll();
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Employee");

        NumberAxis yAxis = new NumberAxis(0, 5000, 500);
        yAxis.setLabel("Earnings");

        BarChart barChart = new BarChart(xAxis, yAxis);

        XYChart.Series series = new XYChart.Series();
        series.setName("Salary per employee");

        for (Employee emp : employees) {
            String name = emp.getAddress().getName();
            String surname = emp.getAddress().getSurname();
            double earnings = emp.getSalary();
            series.getData().add(new XYChart.Data(name + " " + surname, earnings));
        }

        barChart.getData().add(series);

        return barChart;
    }
    /**
     *
     * @return chart that show rents per car
     */
    private BarChart rentChart() {

        List<Car> cars = carRepository.findAll();

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Cars");
        NumberAxis yAxis = new NumberAxis(0, 50, 5);
        yAxis.setLabel("Rents");

        BarChart barChart = new BarChart(xAxis, yAxis);

        XYChart.Series series = new XYChart.Series();
        series.setName("Rents per car");

        for (Car car : cars) {
            List<Rent> rent = rentRepository.findAllByCar(car);
            series.getData().add(new XYChart.Data(car.getBrand() + " " + car.getModel(), rent.size()));
        }

        barChart.getData().add(series);

        return barChart;
    }
}
