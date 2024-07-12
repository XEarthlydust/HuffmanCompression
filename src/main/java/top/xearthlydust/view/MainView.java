package top.xearthlydust.view;

import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import top.xearthlydust.controller.MainController;
import top.xearthlydust.entity.file.CompressFile;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainView extends Application {

    public static void run(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        try {
            Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
            Font.loadFont(getClass().getResourceAsStream("/font/SarasaMonoSC-Regular.ttf"), 14);
            Font.loadFont(getClass().getResourceAsStream("/font/JetBrainsMonoNLNerdFont-Regular.ttf"), 14);
            final URL url = getClass().getResource("/main.fxml");
            final Parent root = FXMLLoader.load(Objects.requireNonNull(url));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Huffman Compression");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
