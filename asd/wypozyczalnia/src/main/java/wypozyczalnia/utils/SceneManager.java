package wypozyczalnia.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import wypozyczalnia.config.StageConfig;

import java.io.IOException;

@Component
public class SceneManager {
    public static Stage stage;
    private StageConfig stageConfig;
    private ApplicationContext context;

    public void init(Stage stage) {
        SceneManager.stage = stage;
        Scene scene = new Scene(
                getView(stageConfig.getView().getFxmlPath()),
                stageConfig.getWidth(),
                stageConfig.getHeight());
        scene.setFill(Color.TRANSPARENT);
        stage.initStyle(StageStyle.TRANSPARENT);
        SceneManager.stage.setScene(scene);
        SceneManager.stage.setTitle(stageConfig.getApplicationTitle());
        SceneManager.stage.setMaximized(false);
        SceneManager.stage.setFullScreen(false);
        SceneManager.stage.show();
    }

    public void show(SceneType sceneType) {
        Parent view = getView(sceneType.getFxmlPath());
        stage.getScene().setRoot(view);
    }

    private Parent getView(String viewName) {
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(context::getBean);
        loader.setLocation(getClass().getResource("/fxml/" + viewName + ".fxml"));
        try {
            return (Parent) loader.load();
        } catch (IOException e) {
            return null;
        }
    }

    @Autowired
    public void setContext(ApplicationContext context) {
        this.context = context;
    }

    @Autowired
    public void setStageConfig(StageConfig stageConfig) {
        this.stageConfig = stageConfig;
    }
}
