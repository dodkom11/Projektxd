package wypozyczalnia.utils;

public enum SceneType {

    MAIN("app/main"),
    WORKER_MAIN("app/workerMain"),
    WORKER_CLIENT_MANAGEMENT("app/clientManagement"),
    WORKER_OUR_CARS("app/ourCars"),
    WORKER_RENT_CAR("app/rentCar"),
    WORKER_DIAGRAMS("app/workerDiagrams"),
    ADMIN_GROUPS("app/adminGroups"),
    ADMIN_EMPLOYEE_MANAGEMENT("app/employeesManagement");


    private String fxmlPath;

    SceneType(String path) {
        this.fxmlPath = path;
    }

    String getFxmlPath() {
        return this.fxmlPath;
    }
}
