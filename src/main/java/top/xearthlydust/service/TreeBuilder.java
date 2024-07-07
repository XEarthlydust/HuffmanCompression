package top.xearthlydust.service;

import top.xearthlydust.entity.file.CompressFile;
import top.xearthlydust.entity.huffman.runtime.NodeWithFreq;
import top.xearthlydust.entity.huffman.Tree;
import top.xearthlydust.util.HuffmanUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;

public class TreeBuilder {
    private static final Integer chunkSize = 1024 * 1024;

    public static List<Tree> buildFileTree(CompressFile compressFile) {

        try (FileInputStream fis = new FileInputStream(compressFile.getFileName())) {
            File file = new File(compressFile.getFileName());
            int num = (int) Math.ceil((double) file.length() / chunkSize);

            byte[] buffer = new byte[chunkSize];
            BufferedInputStream bis = new BufferedInputStream(fis);
            List<Tree> list = new Vector<>();
            CountDownLatch latch = new CountDownLatch(num);
            int count = 0;

            while (true) {
                count++;
                int readLength = bis.read(buffer);
                if (readLength != -1) {
                    byte[] finalBuffer = buffer.clone();
                    int finalCount = count;
                    ThreadPoolManager.runOneTask(() -> {
                        PriorityQueue<NodeWithFreq> queue = HuffmanUtil.checkStreamToMap(finalBuffer, readLength);
                        Tree tree = HuffmanUtil.buildHuffmanTree(queue);
                        Map<Byte, Byte[]> map = HuffmanUtil.buildCodeTable(tree.getRoot());
                        tree.setCodeTable(map);
                        while (true) if (list.size() + 1 == finalCount) {
                            list.add(tree);
                            break;
                        }
                        latch.countDown();
                    });
                } else break;
            }
            latch.await();

            bis.close();
            return list;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
