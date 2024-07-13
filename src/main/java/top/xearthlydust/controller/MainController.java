package top.xearthlydust.controller;

import com.esotericsoftware.kryo.io.Input;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import top.xearthlydust.entity.file.CompressFile;
import top.xearthlydust.service.Compressor;
import top.xearthlydust.service.Decompressor;
import top.xearthlydust.service.ThreadPoolManager;
import top.xearthlydust.util.FileUtil;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainController {
    @FXML
    private Label labelInfo;
    @FXML
    private MenuItem menuItemUnzip;
    @FXML
    private MenuItem menuItemZip;
    @FXML
    private MenuItem menuFolderZip;
    @FXML
    private MenuItem menuItemClose;
    @FXML
    private MenuItem menuCheckStruct;
    @FXML
    private MenuItem menuForceStop;
    @FXML
    private MenuItem menuChangeThreads;
    @FXML
    private MenuItem menuItemGitHub;

    @FXML
    private MenuItem menuItemMyself;
    @FXML
    private Button btnExecute;
    @FXML
    private TextField textFieldInput;
    @FXML
    private TextField textFieldOutput;
    @FXML
    private Button btnSelectInput;
    @FXML
    private Button btnSelectOutput;

    private String selectedMode = "";
    private List<File> tmpFiles = new ArrayList<>();

    @FXML
    private void initialize() {
        btnExecute.setDisable(true);
        textFieldInput.setDisable(true);
        textFieldOutput.setDisable(true);
        btnSelectInput.setDisable(true);
        btnSelectOutput.setDisable(true);
        labelInfo.setFont(Font.font(20));

        // 监听模式菜单项选择事件
        menuItemUnzip.setOnAction(event -> {
            selectedMode = "解压";
            labelInfo.setText(selectedMode);
            enableAll();
        });
        menuItemZip.setOnAction(event -> {
            selectedMode = "压缩文件";
            labelInfo.setText(selectedMode);
            enableAll();
        });
        menuFolderZip.setOnAction(event -> {
            selectedMode = "压缩目录";
            labelInfo.setText(selectedMode);
            enableAll();
        });
        menuCheckStruct.setOnAction(event -> {
            selectedMode = "查看结构";
            labelInfo.setText(selectedMode);
            enableAll();
            textFieldOutput.setDisable(true);
            btnSelectOutput.setDisable(true);
        });

        menuItemClose.setOnAction(event -> {
            btnExecute.setDisable(true);
            selectedMode = "";
            ThreadPoolManager.closeThreadPool();
            Platform.exit();
            System.exit(0);
        });

        // 监听 “选项” 菜单时间
        menuForceStop.setOnAction(actionEvent -> {
            ThreadPoolManager.closeAll();
            disableAll();
            selectedMode = "";
            labelInfo.setText("已清空线程池 请重选择模式");
        });

        menuChangeThreads.setOnAction(event -> changeThreads());

        // 监听 "关于" 菜单 选择事件
        menuItemGitHub.setOnAction(actionEvent -> openWebpage("https://github.com/XEarthlydust/HuffmanCompression"));
        menuItemMyself.setOnAction(actionEvent -> openWebpage("https://github.com/XEarthlydust"));

        // 捕获选择按钮的点击事件
        btnSelectInput.setOnAction(event -> {
            switch (selectedMode) {
                case "压缩文件" -> selectMultipleFile(textFieldInput);
                case "压缩目录" -> selectFolder("选择需要压缩的文件夹", textFieldInput);
                case "解压", "查看结构" -> selectAsHFM(textFieldInput);
            }
        });

        btnSelectOutput.setOnAction(event -> {
            switch (selectedMode) {
                case "压缩目录", "压缩文件" -> saveAsHFM(textFieldOutput);
                case "解压", "查看结构" -> selectFolder("选择输出文件夹", textFieldOutput);
            }
        });

        // 捕获执行按钮的点击事件
        btnExecute.setOnAction(event -> {
            switch (selectedMode) {
                case "压缩目录" -> ThreadPoolManager.runOneTask(() -> {
                    disableAll();
                    try {
                        Compressor.finalCompressWithSave(textFieldInput.getText(), textFieldOutput.getText());
                        showAlert(Alert.AlertType.INFORMATION, "压缩成功", "输出: " + textFieldOutput.getText());
                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    } finally {
                        enableAll();
                    }
                });
                case "解压" -> {
                    final String fileInputPath = textFieldInput.getText();
                    final String fileOutputPath = textFieldOutput.getText();
                    ThreadPoolManager.runOneTask(() -> {
                        disableAll();
                        try (FileInputStream fis = new FileInputStream(fileInputPath); Input input = new Input(fis)) {
                            CompressFile compressFile = (CompressFile) FileUtil.deserializeOneObj(input);
                            Decompressor.finalDecompressWithSave(compressFile, fileInputPath, fileOutputPath);
                            showAlert(Alert.AlertType.INFORMATION, "解压成功", "输出: " + fileInputPath);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } finally {
                            enableAll();
                        }
                    });
                }
                case "压缩文件" -> {
                    final String fileOutputPath = textFieldOutput.getText();
                    ThreadPoolManager.runOneTask(() -> {
                        disableAll();
                        try {
                            CompressFile compressFile = new CompressFile(tmpFiles);
                            Compressor.oneMultipleFileCompressWithSave(compressFile, tmpFiles.get(0).getAbsoluteFile().getParentFile().getPath(), fileOutputPath);
                            showAlert(Alert.AlertType.INFORMATION, "解压成功", "输出目录: " + fileOutputPath);
                        } catch (InterruptedException | IOException e) {
                            throw new RuntimeException(e);
                        }
                        enableAll();
                    });
                }
            }
        });
    }

    private void openWebpage(String url) {
        try {
            URI uri = new URI(url);
            Desktop.getDesktop().browse(uri);
        } catch (URISyntaxException | IOException ignored) {
        }
    }

    private void selectFolder(String title, TextField textField) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(title);
        Stage stage = (Stage) textField.getScene().getWindow();
        File selectedFolder = directoryChooser.showDialog(stage);
        if (selectedFolder != null) {
            System.out.println(selectedFolder.getAbsolutePath());
            textField.setText(selectedFolder.getAbsolutePath());
        }
    }

    private void selectMultipleFile(TextField textField) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择需要的文件");
        Stage stage = (Stage) textField.getScene().getWindow();
        tmpFiles = fileChooser.showOpenMultipleDialog(stage);
        if (!tmpFiles.isEmpty()) {
            textField.setText("\"" + tmpFiles.get(0).getAbsoluteFile().getParentFile().getPath() + "\"目录下 一个或多个文件");
        }
    }

    private void saveAsHFM(TextField textField) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("保存到...");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("哈夫曼压缩包", "*.hfm"));
        Stage stage = (Stage) textField.getScene().getWindow();
        File selectedFile = fileChooser.showSaveDialog(stage);
        if (selectedFile != null && selectedFile.exists() && selectedFile.delete()) {
            textField.setText(selectedFile.getAbsolutePath());
        } else if (selectedFile != null) {
            textField.setText(selectedFile.getAbsolutePath());
        }
    }

    private void selectAsHFM(TextField textField) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择一个HFM文件");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("哈夫曼压缩包", "*.hfm"));
        Stage stage = (Stage) textField.getScene().getWindow();
        File selectedFolder = fileChooser.showOpenDialog(stage);
        if (selectedFolder != null) {
            System.out.println(selectedFolder.getAbsolutePath());
            textField.setText(selectedFolder.getAbsolutePath());
        }
    }

    private void enableAll() {
        btnExecute.setDisable(false);
        btnSelectInput.setDisable(false);
        btnSelectOutput.setDisable(false);
        textFieldInput.setDisable(false);
        textFieldOutput.setDisable(false);
    }

    private void disableAll() {
        btnExecute.setDisable(true);
        btnSelectInput.setDisable(true);
        btnSelectOutput.setDisable(true);
        textFieldInput.setDisable(true);
        textFieldOutput.setDisable(true);
    }

    private void changeThreads() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/thread.fxml"));
            Parent root = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("修改线程数");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            dialogStage.setResizable(false);
            dialogStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/image/setting.png"))));
            DialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
            if (controller.isOkClicked()) {
                int threads = controller.getThreads();
                if (threads > 0) {
                    ThreadPoolManager.restartThreadPool(threads);
                }
                labelInfo.setText("最大线程数已被修改为" + threads);
            }
        } catch (IOException ignored) {
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(type);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.show();
        });
    }
}
