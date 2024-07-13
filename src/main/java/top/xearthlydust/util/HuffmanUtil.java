package top.xearthlydust.util;

import top.xearthlydust.entity.file.FileChunk;
import top.xearthlydust.entity.huffman.Node;
import top.xearthlydust.entity.huffman.Tree;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import java.util.ArrayList;
import java.util.List;

public class HuffmanUtil {


    // 构建哈夫曼树和编码表
    public static void buildHuffmanTree(FileChunk fileChunk) {
        byte[] bytes = fileChunk.getBytes();
        // 统计每个字节出现的频率
        Map<Byte, Integer> frequencyMap = new HashMap<>();
        for (byte b : bytes) {
            frequencyMap.put(b, frequencyMap.getOrDefault(b, 0) + 1);
        }

        PriorityQueue<Node> minHeap = new PriorityQueue<>();

        for (Map.Entry<Byte, Integer> entry : frequencyMap.entrySet()) {
            Node node = new Node(entry.getKey());
            node.setFreq(entry.getValue());
            minHeap.offer(node);
        }
        // 构建哈夫曼树
        while (minHeap.size() > 1) {
            Node left = minHeap.poll();
            Node right = minHeap.poll();
            Node parent = new Node();
            parent.setFreq(left.getFreq() + right.getFreq());
            parent.setLeft(left);
            parent.setRight(right);
            minHeap.offer(parent);
        }
        // 根节点即为哈夫曼树的根
        Node root = minHeap.poll();
        // 生成编码表
        Map<Byte, Byte[]> codeTable = new HashMap<>();
        buildCodeTable(root, new Byte[0], codeTable);
        // 创建并返回哈夫曼树对象
        Tree huffmanTree = new Tree(root);
        huffmanTree.setCodeTable(codeTable);

        fileChunk.setTree(huffmanTree);
    }

    // 递归构建编码表
    private static void buildCodeTable(Node node, Byte[] code, Map<Byte, Byte[]> codeTable) {
        if (node.getLeft() == null && node.getRight() == null) {
            codeTable.put(node.getData(), code);
            return;
        }
        if (node.getLeft() != null) {
            Byte[] leftCode = new Byte[code.length + 1];
            System.arraycopy(code, 0, leftCode, 0, code.length);
            leftCode[code.length] = 0;
            buildCodeTable(node.getLeft(), leftCode, codeTable);
        }
        if (node.getRight() != null) {
            Byte[] rightCode = new Byte[code.length + 1];
            System.arraycopy(code, 0, rightCode, 0, code.length);
            rightCode[code.length] = 1;
            buildCodeTable(node.getRight(), rightCode, codeTable);
        }
    }

    // 根据编码表对字节数组进行编码，并返回编码后的字节数组
    public static byte[] encodeBytes(Map<Byte, Byte[]> codeTable, byte[] bytes) {
        List<Byte> encodedBytesList = new ArrayList<>();
        int bitPosition = 0;
        byte currentByte = 0;

        // 编码数据
        for (byte b : bytes) {
            Byte[] code = codeTable.get(b);
            for (byte bit : code) {
                currentByte = (byte) (currentByte | (bit << (7 - bitPosition)));
                bitPosition++;

                if (bitPosition == 8) {
                    encodedBytesList.add(currentByte);
                    currentByte = 0;
                    bitPosition = 0;
                }
            }
        }

        // 处理最后一个字节
        if (bitPosition > 0) encodedBytesList.add(currentByte);

        // 转换为字节数组
        byte[] encodedBytes = new byte[encodedBytesList.size()];
        for (int i = 0; i < encodedBytesList.size(); i++) {
            encodedBytes[i] = encodedBytesList.get(i);
        }

        return encodedBytes;
    }

    // 解码字节数组，并根据给定的哈夫曼树进行解码
    public static byte[] decodeBytes(Tree huffmanTree, byte[] encodedBytes, int originalLength) {
        List<Byte> decodedBytesList = new ArrayList<>();
        Node current = huffmanTree.getRoot();
        int bitPosition = 0;
        byte currentByte;

        // 解码数据
        for (int i = 0; i < encodedBytes.length && decodedBytesList.size() < originalLength; i++) {
            currentByte = encodedBytes[i];
            int mask = 0x80; // 10000000

            while (bitPosition < 8 && decodedBytesList.size() < originalLength) {
                int bit = (currentByte & mask) == 0 ? 0 : 1;
                mask >>= 1;
                bitPosition++;
                if (bit == 0) current = current.getLeft();
                else current = current.getRight();

                if (current.getLeft() == null && current.getRight() == null) {
                    decodedBytesList.add(current.getData());
                    current = huffmanTree.getRoot();
                }
            }
            bitPosition = 0;
        }

        // 转换为字节数组
        byte[] decodedBytes = new byte[decodedBytesList.size()];
        for (int i = 0; i < decodedBytesList.size(); i++) {
            decodedBytes[i] = decodedBytesList.get(i);
        }
        return decodedBytes;
    }

    public static byte[] cutNull(byte[] bytes, int usedBit) {
        byte[] newBytes = new byte[usedBit];
        System.arraycopy(bytes, 0, newBytes, 0, usedBit);
        return newBytes;
    }
}