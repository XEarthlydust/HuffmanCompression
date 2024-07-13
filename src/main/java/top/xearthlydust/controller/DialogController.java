package top.xearthlydust.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

public class DialogController {

    @FXML
    private Label sliderValueLabel;
    @FXML
    private Slider slider;

    @Setter
    private Stage dialogStage;
    @Getter
    private boolean okClicked = false;

    @Getter
    private int threads = 16;

    @FXML
    private void initialize() {
        okClicked = false;
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            sliderValueLabel.setText("线程数: " + newValue.intValue());
            threads = newValue.intValue();
        });
    }

    @FXML
    private void handleCancel() {
        okClicked = true;
        dialogStage.close();
    }
}
