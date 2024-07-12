package top.xearthlydust.service;

import com.esotericsoftware.kryo.io.Input;
import top.xearthlydust.entity.file.CompressFile;
import top.xearthlydust.entity.file.FileChunk;
import top.xearthlydust.util.FileUtil;
import top.xearthlydust.util.HuffmanUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class Decompressor {

    public static void oneSliceDecompressWithSave(FileChunk fileChunk, String savePath) {
        fileChunk.setBytes(HuffmanUtil.decodeBytes(
                fileChunk.getTree(),
                fileChunk.getBytes(),
                fileChunk.getLength()
        ));
        FileUtil.saveOneChunk(fileChunk, savePath);
    }

    public static void finalDecompressWithSave(CompressFile compressFile,String filePath, String savePath) {
        Map<Integer, String> fileMap = new HashMap<>();
        recursionGetIds(compressFile, fileMap, "");

        try (FileInputStream fis = new FileInputStream(filePath);
             Input input = new Input(fis)
        ) {
            FileUtil.deserializeOneObj(input);
            while (input.available() > 0) {
                final FileChunk fileChunk = (FileChunk) FileUtil.deserializeOneObj(input);
                // 待测试
                ThreadPoolManager.runOneTask(()->{
                    if (fileMap.containsKey(fileChunk.getFileId())) {
                        synchronized (savePath + "/" + fileMap.get(fileChunk.getFileId())) {
                            Decompressor.oneSliceDecompressWithSave(fileChunk,
                                    savePath + "/" + fileMap.get(fileChunk.getFileId()));
                        }
                    }
                });
                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static void recursionGetIds(CompressFile compressFile, Map<Integer, String> fileMap, String nowFolderPath) {
        if (compressFile.isFolder()) {
            for (CompressFile compressFileChild : compressFile.getChildren()) {
                recursionGetIds(compressFileChild, fileMap, nowFolderPath + "/" + compressFile.getFileName());
            }
        } else {
            fileMap.put(compressFile.getFileId(), nowFolderPath + "/" + compressFile.getFileName());
        }
    }
}
