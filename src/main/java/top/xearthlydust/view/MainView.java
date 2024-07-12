package top.xearthlydust.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;

public class MainView extends Application {

    public static void run(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        try {
//            Application.setUserAgentStylesheet(new CupertinoLight().getUserAgentStylesheet());
            Font.loadFont(getClass().getResourceAsStream("/font/SarasaMonoSC-Regular.ttf"), 14);
            Font.loadFont(getClass().getResourceAsStream("/font/JetBrainsMonoNLNerdFont-Regular.ttf"), 14);
            Application.setUserAgentStylesheet("/css/cupertino-light.css");
            final URL url = getClass().getResource("/main.fxml");
            final Parent root = FXMLLoader.load(Objects.requireNonNull(url));
            Scene scene = new Scene(root);
//            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
