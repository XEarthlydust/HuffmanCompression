package top.xearthlydust.service;

import top.xearthlydust.entity.file.CompressFile;
import top.xearthlydust.entity.file.FileChunk;
import top.xearthlydust.util.FileUtil;
import top.xearthlydust.util.HuffmanUtil;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;

public class Compressor {
    public static void oneFileCompressWithSave(CompressFile compressFile, String filePath, String savePath) throws InterruptedException {
        Queue<FileChunk> queue = TreeBuilder.buildFileTree(compressFile, filePath);
        CountDownLatch latch = new CountDownLatch(queue.size());
        while (!queue.isEmpty()) {
            FileChunk fileChunk = queue.poll();
            ThreadPoolManager.runOneTask(() -> {
                fileChunk.setBytes(HuffmanUtil.encodeBytes(fileChunk.getTree().getCodeTable(), fileChunk.getBytes()));
                try {
                    fileChunk.getTree().clearMap();
                    FileUtil.serializeOneObj(fileChunk, savePath);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                latch.countDown();
            });
        }
        latch.await();
    }
}
