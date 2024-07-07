package top.xearthlydust.service;

import top.xearthlydust.entity.file.FileSlice;
import top.xearthlydust.util.FileUtil;

import java.io.FileInputStream;
import java.io.IOException;

public class Decompressor {

    public static void oneFile(String fileName) {
        try (FileInputStream fis = new FileInputStream(fileName)){
            FileSlice fileSlice = FileUtil.readOneSlice(fis);
            ThreadPoolManager.runOneTask(
                    () -> {
                    }
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
