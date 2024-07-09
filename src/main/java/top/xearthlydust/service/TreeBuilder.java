package top.xearthlydust.service;

import top.xearthlydust.entity.file.CompressFile;
import top.xearthlydust.entity.file.FileChunk;
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
    public static final Integer CHUNK_SIZE = 1024 * 1024;

    public static PriorityBlockingQueue<FileChunk> buildFileTree(CompressFile compressFile) {

        try (FileInputStream fis = new FileInputStream(compressFile.getFileName())) {
            File file = new File(compressFile.getFileName());
            int num = (int) Math.ceil((double) file.length() / CHUNK_SIZE);

            byte[] buffer = new byte[CHUNK_SIZE];
            BufferedInputStream bis = new BufferedInputStream(fis);

            PriorityBlockingQueue<FileChunk> threadQueue = new PriorityBlockingQueue<>();

            CountDownLatch latch = new CountDownLatch(num);
            int count = 0;

            while (true) {
                count++;
                int readLength = bis.read(buffer);
                if (readLength != -1) {
                    byte[] finalBuffer = buffer.clone();
                    int finalCount = count;
                    ThreadPoolManager.runOneTask(() -> {
                        FileChunk fileChunk = new FileChunk(finalCount, null);
                        fileChunk.setBytes(BitUtil.cutNull(finalBuffer, readLength));
                        PriorityQueue<NodeWithFreq> queue = HuffmanUtil.checkStreamToMap(finalBuffer, readLength);
                        Tree tree = HuffmanUtil.buildHuffmanTree(queue);
                        Map<Byte, Byte[]> map = HuffmanUtil.buildCodeTable(tree.getRoot());
                        tree.setCodeTable(map);
                        fileChunk.setTree(tree);
                        threadQueue.add(fileChunk);
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
