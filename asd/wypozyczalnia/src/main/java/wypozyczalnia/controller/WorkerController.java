package wypozyczalnia.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import wypozyczalnia.config.StoredData;
import wypozyczalnia.config.VetoListener;
import wypozyczalnia.model.*;
import wypozyczalnia.model.Priority;
import wypozyczalnia.repository.*;
import wypozyczalnia.service.AccountService;
import wypozyczalnia.utils.SceneManager;
import wypozyczalnia.utils.SceneType;

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
    private AccountService accountService;

    private SceneManager sceneManager;

    private double x = 0;
    private double y = 0;
    boolean access = true;
    boolean plusTask = false;

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
    @FXML
    private Label panelLabel;
    @FXML
    private ImageView plusBtn;

    private Task task;

    private Task newTask;

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
    void toDoClicked(MouseEvent event){
        access = true;
        if(toDoList.getSelectionModel().getSelectedItem() != null){
            fillTask(toDoList);
        }
    }
    @FXML
    void inProgressClicked(MouseEvent event){
        access = true;
        if(inProgressList.getSelectionModel().getSelectedItem() != null) {
            fillTask(inProgressList);
        }
    }


    @FXML
    void doneClicked(MouseEvent event){
        access = true;
        if(doneList.getSelectionModel().getSelectedItem() != null) {
            fillTask(doneList);
        }
    }

    /**
     * we can add comments with enter
     */
    @FXML
    void onEnter(ActionEvent event){
        String commWhite = taskComment.getText();
        commWhite = commWhite.replaceAll(" ","");
        if(!commWhite.equals("")) {
            Comment comment = new Comment(accountRepository.findById(StoredData.getLoggedUserId()), taskComment.getText(), taskRepository.getOne(task.getId()));
            commentRepository.save(comment);
            loadComments();
            taskComment.clear();
        }

    }
    @FXML
    void plusBtnClicked(){
        taskTitle.clear();
        taskContent.clear();
        taskStatus.getItems().clear();
        taskPriority.getItems().clear();
        taskAssignedPerson.getItems().clear();
        access = true;

        Account account = accountRepository.findById(StoredData.getLoggedUserId());
        List<Account> newAccounts = accountRepository.findAllByBracket_Id(account.getBracket().getId());
        List<State> newState = stateRepository.findAll();
        List<Priority> newPriority =  priorityRepository.findAll();

        for(Account acc : newAccounts){
            taskAssignedPerson.getItems().add(acc.getEmployee().getAddress().getName() + " " + acc.getEmployee().getAddress().getSurname());
        }
        for(State state : newState){
            taskStatus.getItems().add(state.getName());
        }
        for(Priority priority : newPriority){
            taskPriority.getItems().add(priority.getName());
        }
        newTask = new Task();
        newTask.setBracket(account.getBracket());

        plusTask = true;
        taskPane.setVisible(true);

    }


    @FXML
    void confirmClicked(MouseEvent event) {
        String str = taskAssignedPerson.getSelectionModel().getSelectedItem();
        String[] splited = str.split("\\s+");
        Account addAcc = accountRepository.findByEmployeeAddressNameAndEmployeeAddressSurname(splited[0], splited[1]);

        if(plusTask){
            newTask.setContent(taskContent.getText());
            newTask.setPriority(priorityRepository.findByName(taskPriority.getSelectionModel().getSelectedItem()));
            newTask.setTitle(taskTitle.getText());
            newTask.setState(stateRepository.findByName(taskStatus.getSelectionModel().getSelectedItem()));
            newTask.setAccount(addAcc);
            accountService.saveTask(newTask);
            plusTask = false;

        }else {
            task.setContent(taskContent.getText());
            task.setPriority(priorityRepository.findByName(taskPriority.getSelectionModel().getSelectedItem()));
            task.setTitle(taskTitle.getText());
            task.setState(stateRepository.findByName(taskStatus.getSelectionModel().getSelectedItem()));

            task.setAccount(addAcc);
            taskRepository.save(task);

        }
        fillTask();
        taskPane.setVisible(false);
    }

    @FXML
    void logoutClicked(){ sceneManager.show(SceneType.MAIN); }
    @FXML
    void clientsClicked(){
        sceneManager.show(SceneType.WORKER_CLIENT_MANAGEMENT);
    }
    @FXML
    void ourCarsClicked(){
        sceneManager.show(SceneType.WORKER_OUR_CARS);
    }
    @FXML
    void rentCarClicked(){
        sceneManager.show(SceneType.WORKER_RENT_CAR);
    }
    @FXML
    void diagramsClicked(){
        sceneManager.show(SceneType.WORKER_DIAGRAMS);
    }

    @FXML
    public void initialize() {
        if(StoredData.isAdmin()){
            panelLabel.setText("Admin Panel");
        }else{
            panelLabel.setText("Worker Panel");
        }

        taskPane.setVisible(false);

        if(accountRepository.findById(StoredData.getLoggedUserId()).getPermission().getName().equals("worker")){
            plusBtn.setVisible(false);
        }else{
            plusBtn.setVisible(true);
        }


        taskPriority.getSelectionModel().selectedItemProperty().addListener(new VetoListener<String>(taskPriority.getSelectionModel()) {

            @Override
            protected boolean isInvalidChange(String oldValue, String newValue) {
                return !access;
            }

        });
        taskAssignedPerson.getSelectionModel().selectedItemProperty().addListener(new VetoListener<String>(taskAssignedPerson.getSelectionModel()) {

            @Override
            protected boolean isInvalidChange(String oldValue, String newValue) {
                return !access;
            }

        });

        fillComboBox();
        fillTask();
    }

    @Autowired
    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    private void fillTask(ListView<Label> list){

        Account account = accountRepository.findById(StoredData.getLoggedUserId());

        task = taskRepository.findByTitle(list.getSelectionModel().getSelectedItem().getText());
        Optional<State> state = stateRepository.findById(task.getState().getId());
        Optional<Priority> priority = priorityRepository.findById(task.getPriority().getId());
        taskAssignedPerson.getSelectionModel().select(accountRepository.findByTask(task).getEmployee().getAddress().getName() + " " + accountRepository.findByTask(task).getEmployee().getAddress().getSurname());
        taskStatus.getSelectionModel().select(state.get().getName());
        taskPriority.getSelectionModel().select(priority.get().getName());
        taskTitle.setText(task.getTitle());
        taskContent.setText(task.getContent());
        taskPane.setVisible(true);

        loadComments();

        if(account.getPermission().getName().equals("worker") && !StoredData.isLeader()){
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

    private void fillComboBox(){
        Account account = accountRepository.findById(StoredData.getLoggedUserId());
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
        if(account.getPermission().getName().equals("worker") && !StoredData.isLeader()){
            taskContent.setEditable(false);
            taskTitle.setEditable(false);
        }

        taskAssignedPerson.setItems(listOfAccounts);
        taskPriority.setItems(listOfPriority);
        taskStatus.setItems(listOfStatus);


    }

    private void fillTask(){
        doneList.getItems().clear();
        toDoList.getItems().clear();
        inProgressList.getItems().clear();
        Account account = accountRepository.findById(StoredData.getLoggedUserId());
        List<Task> tasks = taskRepository.findAllByBracket_Id(account.getBracket().getId());
        int x = 0;
        Label label[] = new Label[tasks.size()];
        for (Task task1 : tasks) {
            label[x] = new Label(task1.getTitle());
            if(task1.getState().getName().equals("to do")){
                toDoList.getItems().add(label[x]);
            }
            if(task1.getState().getName().equals("in progress")){
                inProgressList.getItems().add(label[x]);
            }
            if(task1.getState().getName().equals("done")){
                doneList.getItems().add(label[x]);
            }
            x++;
        }
    }

}
