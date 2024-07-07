package top.xearthlydust.service;

import top.xearthlydust.entity.file.CompressFile;
import top.xearthlydust.entity.file.FileSlice;
import top.xearthlydust.util.BitUtil;

import java.util.List;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;

public class Compressor {
    public static List<FileSlice> oneFileCompress(String fileName, String savePath) throws InterruptedException {
        Queue<FileSlice> queue = TreeBuilder.buildFileTree(new CompressFile(fileName));
        CountDownLatch latch = new CountDownLatch(queue.size());
        List<FileSlice> fileSliceList = new Vector<>();
        while (!queue.isEmpty()) {
            FileSlice fileSlice = queue.poll();
            ThreadPoolManager.runOneTask(() -> {
                fileSlice.setBytes(BitUtil.getCodeByTable(fileSlice.getTree().getCodeTable(), fileSlice.getBytes()));
                fileSliceList.add(fileSlice);
                latch.countDown();
            });
        }
        latch.await();
        return fileSliceList;
    }
}
