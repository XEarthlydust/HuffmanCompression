package top.xearthlydust.service;

import top.xearthlydust.entity.file.CompressFile;
import top.xearthlydust.entity.file.FileChunk;
import top.xearthlydust.util.BitUtil;

import java.util.List;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;

public class Compressor {
    public static List<FileChunk> oneFileCompressWithSave(String fileName, String savePath) throws InterruptedException {
        Queue<FileChunk> queue = TreeBuilder.buildFileTree(new CompressFile(fileName, false));
        CountDownLatch latch = new CountDownLatch(queue.size());
        List<FileChunk> fileChunkList = new Vector<>();
        while (!queue.isEmpty()) {
            FileChunk fileChunk = queue.poll();
            ThreadPoolManager.runOneTask(() -> {
                fileChunk.setBytes(BitUtil.getCodeByTable(fileChunk.getTree().getCodeTable(), fileChunk.getBytes()));
                fileChunkList.add(fileChunk);
                latch.countDown();
            });
        }
        latch.await();
        return fileChunkList;
    }
}
