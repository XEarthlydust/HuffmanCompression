package top.xearthlydust.service;

import top.xearthlydust.entity.file.FileSlice;
import top.xearthlydust.entity.huffman.NodeWithFreq;
import top.xearthlydust.entity.huffman.Tree;
import top.xearthlydust.util.HuffmanUtil;
import top.xearthlydust.util.NodeException;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;

public class TreeBuilder {
    private static final Integer chunkSize = 1024*1024;
    public static Map<Tree,Map<Byte, String>> buildTree(FileInputStream fis) throws InterruptedException {
        Map<Tree,Map<Byte, String>> treeMap = new Hashtable<>();

        List<FileSlice> fileSlices = new Vector<>();

        try (BufferedInputStream bis = new BufferedInputStream(fis)) {
            byte[] buffer = new byte[chunkSize];
            int bytesRead;
            int chunkIndex = 0;
            CountDownLatch latch = new CountDownLatch(1);
            while ((bytesRead = bis.read(buffer)) > 0) {
//                executor.submit(new ProcessChunkTask(buffer, bytesRead, chunkIndex));

                byte[] finalBuffer = buffer.clone();
                int finalBytesRead = bytesRead;
                ThreadPoolManager.runOneTask(
                        () -> {
                            try {
                                PriorityQueue<NodeWithFreq> tmp = HuffmanUtil.checkStreamToMap(finalBuffer, finalBytesRead);
                                System.out.println();
                                Tree tree = HuffmanUtil.buildHuffmanTree(
                                        tmp
                                );
                                Map<Byte, String> codeTable = HuffmanUtil.buildCodeTable(tree.getRoot());
                                treeMap.put(tree, codeTable);
                            } catch (NodeException e) {
                                throw new RuntimeException(e);
                            } finally {
                                latch.countDown();
                            }
                        }
                );

                buffer = new byte[chunkSize];
                chunkIndex++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Thread.sleep(5000);
        return treeMap;
    }
}
