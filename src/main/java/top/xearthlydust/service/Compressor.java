package top.xearthlydust.service;

import top.xearthlydust.entity.file.CompressFile;
import top.xearthlydust.entity.file.FileChunk;
import top.xearthlydust.util.FileUtil;
import top.xearthlydust.util.HuffmanUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;

public class Compressor {
    public static void oneFileCompressWithSave(CompressFile compressFile, String filePath, String savePath) throws InterruptedException {
        Queue<FileChunk> queue = TreeBuilder.buildFileTree(compressFile, filePath);
        CountDownLatch latch = new CountDownLatch(queue.size());
        Object object = new Object();
        while (!queue.isEmpty()) {
            FileChunk fileChunk = queue.poll();
            ThreadPoolManager.runOneTask(() -> {
                fileChunk.setBytes(HuffmanUtil.encodeBytes(fileChunk.getTree().getCodeTable(), fileChunk.getBytes()));
                fileChunk.getTree().clearMap();
                synchronized (object) {
                    try {
                        FileUtil.serializeOneObj(fileChunk, savePath);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                latch.countDown();
            });
        }
        latch.await();
    }

    public static void finalCompressWithSave(String filePath, String savePath) throws IOException, InterruptedException {
        Path path = Path.of(filePath);
        CompressFile compressFile = FileUtil.getDirectoryStructure(path);
        FileUtil.serializeOneObj(compressFile, savePath);
        recursionCompress(compressFile, path.getParent().toString(), savePath);
    }

    private static void recursionCompress(CompressFile compressFile, String filePath, String savePath) throws InterruptedException {
        if (compressFile.isFolder()) {
            for (CompressFile compressFileChild : compressFile.getChildren()) {
                recursionCompress(compressFileChild, filePath + "/" + compressFile.getFileName(), savePath);
            }
        } else {
            oneFileCompressWithSave(compressFile, filePath + "/" + compressFile.getFileName(), savePath);
        }
    }
}
