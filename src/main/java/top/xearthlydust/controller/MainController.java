package top.xearthlydust.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class MainController {

    @FXML
    private Label statusLabel;

    @FXML
    private ComboBox<String> optionBox1;

    @FXML
    private ComboBox<String> optionBox2;

    private boolean isCompressMode = true; // 默认为压缩模式

    // 初始化方法，可以根据需要初始化选项框等内容
    @FXML
    private void initialize() {
        // 初始化选项框等
    }

    // 切换到解压模式
    @FXML
    private void switchToDecompressMode() {
        isCompressMode = false;
        statusLabel.setText("当前状态：解压模式");
        optionBox1.getItems().clear();
        optionBox1.getItems().add("*.hfm 文件");
        optionBox2.setDisable(false);
        optionBox2.setPromptText("选择文件夹");
    }

    // 切换到压缩模式
    @FXML
    private void switchToCompressMode() {
        isCompressMode = true;
        statusLabel.setText("当前状态：压缩模式");
        optionBox1.getItems().clear();
        optionBox1.getItems().addAll("文件", "文件夹");
        optionBox2.setDisable(true);
        optionBox2.getSelectionModel().clearSelection();
    }

    // 执行操作按钮点击事件
    @FXML
    private void executeOperation() {
        if (isCompressMode) {
            // 执行压缩操作，根据选项框1和2的选择进行相应操作
            String selectedOption1 = optionBox1.getSelectionModel().getSelectedItem();
            String selectedOption2 = optionBox2.getSelectionModel().getSelectedItem();
            // 在这里编写压缩操作的逻辑
            statusLabel.setText("执行了压缩操作");
        } else {
            // 执行解压操作，根据选项框1和2的选择进行相应操作
            String selectedOption1 = optionBox1.getSelectionModel().getSelectedItem();
            String selectedOption2 = optionBox2.getSelectionModel().getSelectedItem();
            // 在这里编写解压操作的逻辑
            statusLabel.setText("执行了解压操作");
        }
    }
}
