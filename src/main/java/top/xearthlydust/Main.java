package top.xearthlydust;

import top.xearthlydust.controller.MainController;
import top.xearthlydust.entity.file.CompressFile;
import top.xearthlydust.service.ThreadPoolManager;
import top.xearthlydust.view.MainView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Main {
    public static void main(String[] args) {
        MainView.run(args);
    }
}