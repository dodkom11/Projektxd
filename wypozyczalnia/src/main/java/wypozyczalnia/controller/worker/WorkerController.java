package wypozyczalnia.controller.worker;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import wypozyczalnia.config.StoredData;
import wypozyczalnia.model.*;
import wypozyczalnia.model.Priority;
import wypozyczalnia.repository.*;
import wypozyczalnia.utils.SceneManager;

import java.awt.*;
import java.util.List;
import java.util.Optional;


@Controller
public class WorkerController {

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
    private EmployeeRepository employeeRepository;

    private SceneManager sceneManager;

    private double x = 0;
    private double y = 0;
    boolean access = true;

    @FXML
    private ListView<Label> toDoList;
    @FXML
    private ListView<Label>  inProgressList;
    @FXML
    private ListView<Label>  doneList;
    @FXML
    private Pane taskPane;
    @FXML
    private TextArea taskTitle;
    @FXML
    private TextArea taskContent;
    @FXML
    private ComboBox<String> taskStatus;
    @FXML
    private ComboBox<String> taskPriority;
    @FXML
    private ComboBox<String> taskAssignedPerson;
    @FXML
    private ListView<Pane> taskComments;
    @FXML
    private TextField taskComment;

    private Task task;

    @FXML
    void dragged(MouseEvent event){
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();

        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);
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
    void toDoClicked(MouseEvent event){
        access = true;
        fillTask(toDoList);
    }
    @FXML
    void inProgressClicked(MouseEvent event){
        access = true;
        fillTask(inProgressList);
    }
    @FXML
    void doneClicked(MouseEvent event){
        access = true;
        fillTask(doneList);
    }

    @FXML
    void onEnter(ActionEvent event){

        Comment comment = new Comment(accountRepository.findById(StoredData.getLoggedUserId()), taskComment.getText(), taskRepository.getOne(task.getId()));
        commentRepository.save(comment);
        loadComments();
        taskComment.clear();

    }

    @FXML
    public void initialize() {
        taskPane.setVisible(false);


        taskPriority.getSelectionModel().selectedItemProperty().addListener(new VetoListener<String>(taskPriority.getSelectionModel()) {

            @Override
            protected boolean isInvalidChange(String oldValue, String newValue) {
                return !access;
            }

        });
        taskAssignedPerson.getSelectionModel().selectedItemProperty().addListener(new VetoListener<String>(taskPriority.getSelectionModel()) {

            @Override
            protected boolean isInvalidChange(String oldValue, String newValue) {
                return !access;
            }

        });

        Account account = accountRepository.findById(StoredData.getLoggedUserId());
        List<Task> tasks = taskRepository.findAllByBracket_Id(account.getBracket().getId());
        List<State> states = stateRepository.findAll();
        List<Priority> priorities = priorityRepository.findAll();
        List<Account> accounts = accountRepository.findAllByBracket_Id(account.getBracket().getId());

        ObservableList<String> listOfAccounts = FXCollections.observableArrayList();
        ObservableList<String> listOfStatus = FXCollections.observableArrayList();
        ObservableList<String> listOfPriority = FXCollections.observableArrayList();

        for (Account acc : accounts) {
            listOfAccounts.add(acc.getEmployee().getAddress().getName() + " " + acc.getEmployee().getAddress().getSurname());
        }
        for (Priority acc : priorities) {
            listOfPriority.add(acc.getName());
        }
        for (State acc : states) {
            listOfStatus.add(acc.getName());
        }
        if(account.getPermission().getName().equals("worker")){
            taskContent.setEditable(false);
            taskTitle.setEditable(false);
        }


        taskAssignedPerson.setItems(listOfAccounts);
        taskPriority.setItems(listOfPriority);
        taskStatus.setItems(listOfStatus);

        int x = 0;
        Label label[] = new Label[tasks.size()];
        for (Task task1 : tasks) {
             label[x] = new Label(task1.getTitle());
             if(task1.getState().getName().equals("to_do")){
                 toDoList.getItems().add(label[x]);
             }
            if(task1.getState().getName().equals("in_progress")){
                 inProgressList.getItems().add(label[x]);
            }
            if(task1.getState().getName().equals("done")){
                 doneList.getItems().add(label[x]);
            }
             x++;
        }


    }

    @Autowired
    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    private void fillTask(ListView<Label> list){
        Employee employee = employeeRepository.findByAccount_Id(StoredData.getLoggedUserId());
        Account account = accountRepository.findById(StoredData.getLoggedUserId());


        task = taskRepository.findByTitle(list.getSelectionModel().getSelectedItem().getText());
        Optional<State> state = stateRepository.findById(task.getState().getId());
        Optional<Priority> priority = priorityRepository.findById(task.getPriority().getId());
        taskAssignedPerson.getSelectionModel().select(employee.getAddress().getName() + " " + employee.getAddress().getSurname());
        taskStatus.getSelectionModel().select(state.get().getName());
        taskPriority.getSelectionModel().select(priority.get().getName());
        taskTitle.setText(task.getTitle());
        taskContent.setText(task.getContent());
        taskPane.setVisible(true);



       loadComments();

        if(account.getPermission().getName().equals("worker")){
             access = false;
        }
    }

    private void loadComments(){
        try {
            taskComments.getItems().clear();


            List<Comment> comments = commentRepository.findByTask_Id(task.getId());
            Text text[] = new Text[comments.size()];
            Label label[] = new Label[comments.size()];
            VBox vBox[] = new VBox[comments.size()];
            int x = 0;
            for (Comment comm : comments) {
                System.out.println(comm.getContent());
                vBox[x] = new VBox();
                if(comm.getAccount().getId() == accountRepository.findById(StoredData.getLoggedUserId()).getId()){
                    vBox[x].setAlignment(Pos.CENTER_RIGHT);
                }else{
                    vBox[x].setAlignment(Pos.CENTER_LEFT);
                }
                text[x] = new Text(comm.getAccount().getEmployee().getAddress().getName() + " " + comm.getAccount().getEmployee().getAddress().getSurname());
                text[x].getStyleClass().add("my-text");
                label[x] = new Label(comm.getContent());
                label[x].setAlignment(Pos.CENTER_RIGHT);
                vBox[x].getChildren().add(text[x]);
                vBox[x].getChildren().add(label[x]);
                taskComments.getItems().add(vBox[x]);
            }


        }catch (Exception e){}

    }

}
