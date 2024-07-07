package top.xearthlydust.service;

import top.xearthlydust.entity.file.CompressFile;
import top.xearthlydust.entity.file.FileSlice;
import top.xearthlydust.entity.huffman.runtime.NodeWithFreq;
import top.xearthlydust.entity.huffman.Tree;
import top.xearthlydust.util.BitUtil;
import top.xearthlydust.util.HuffmanUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.PriorityBlockingQueue;

public class TreeBuilder {
    private static final Integer chunkSize = 1024 * 1024;

    public static PriorityBlockingQueue<FileSlice> buildFileTree(CompressFile compressFile) {

        try (FileInputStream fis = new FileInputStream(compressFile.getFileName())) {
            File file = new File(compressFile.getFileName());
            int num = (int) Math.ceil((double) file.length() / chunkSize);

            byte[] buffer = new byte[chunkSize];
            BufferedInputStream bis = new BufferedInputStream(fis);

            PriorityBlockingQueue<FileSlice> threadQueue = new PriorityBlockingQueue<>();

            CountDownLatch latch = new CountDownLatch(num);
            int count = 0;

            while (true) {
                count++;
                int readLength = bis.read(buffer);
                if (readLength != -1) {
                    byte[] finalBuffer = buffer.clone();
                    int finalCount = count;
                    ThreadPoolManager.runOneTask(() -> {
                        FileSlice fileSlice = new FileSlice(finalCount, null);
                        fileSlice.setBytes(BitUtil.cutNull(finalBuffer, readLength));
                        PriorityQueue<NodeWithFreq> queue = HuffmanUtil.checkStreamToMap(finalBuffer, readLength);
                        Tree tree = HuffmanUtil.buildHuffmanTree(queue);
                        Map<Byte, Byte[]> map = HuffmanUtil.buildCodeTable(tree.getRoot());
                        tree.setCodeTable(map);
                        fileSlice.setTree(tree);
                        threadQueue.add(fileSlice);
                        latch.countDown();
                    });
                } else break;
            }
            latch.await();
            bis.close();
            return threadQueue;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
