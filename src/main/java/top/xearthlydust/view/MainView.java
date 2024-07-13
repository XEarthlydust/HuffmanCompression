package top.xearthlydust.view;

import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import top.xearthlydust.service.ThreadPoolManager;

import java.net.URL;
import java.util.Objects;

public class MainView extends Application {

    public static void run(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        try {
            Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
            final URL url = getClass().getResource("/fxml/main.fxml");
            final Parent root = FXMLLoader.load(Objects.requireNonNull(url));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("Huffman Compression");
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/image/icon.png"))));
            stage.setOnCloseRequest(event -> {
                ThreadPoolManager.closeThreadPool();
                System.exit(0);
            });
            stage.show();
        } catch (Exception ignored) {
        }
    }
}
