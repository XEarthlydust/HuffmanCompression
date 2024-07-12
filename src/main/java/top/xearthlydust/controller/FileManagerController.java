package top.xearthlydust.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class FileManagerController {

    @FXML
    private ListView<String> fileListView;

    public void initialize() {
        // 初始化文件列表，可以从目录中加载文件名到 ListView 中
        fileListView.getItems().addAll("file1.txt", "file2.pdf", "folder1");
    }

    @FXML
    protected void handleOpenButtonAction() {
        String selectedFile = fileListView.getSelectionModel().getSelectedItem();
        if (selectedFile != null) {
            // 处理打开文件或文件夹的操作，这里可以根据需要编写打开文件的逻辑
            System.out.println("Opening: " + selectedFile);
        }
    }

    @FXML
    protected void handleDeleteButtonAction() {
        String selectedFile = fileListView.getSelectionModel().getSelectedItem();
        if (selectedFile != null) {
            // 处理删除文件或文件夹的操作，这里可以根据需要编写删除文件的逻辑
            fileListView.getItems().remove(selectedFile);
            System.out.println("Deleting: " + selectedFile);
        }
    }
}


