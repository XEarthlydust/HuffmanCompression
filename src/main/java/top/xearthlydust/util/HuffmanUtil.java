package top.xearthlydust.util;

import top.xearthlydust.entity.huffman.runtime.NodeWithFreq;
import top.xearthlydust.entity.huffman.Tree;
import top.xearthlydust.entity.huffman.Node;

import java.util.*;

public class HuffmanUtil {
    public static PriorityQueue<NodeWithFreq> checkStreamToMap(byte[] bytes, Integer num) {
        HashMap<Byte, Integer> frequencyMap = new HashMap<>();

        for (int i = 0; i < num; i++) {
            frequencyMap.merge(bytes[i], 1, Integer::sum);
        }

        PriorityQueue<NodeWithFreq> priorityQueue = new PriorityQueue<>();
        frequencyMap.forEach((key, value) -> priorityQueue.offer(new NodeWithFreq(key, value)));
        return priorityQueue;
    }


    public static Tree buildHuffmanTree(PriorityQueue<NodeWithFreq> priorityQueue) {
        while (priorityQueue.size() > 1) {
            NodeWithFreq node1 = priorityQueue.poll();
            NodeWithFreq node2 = priorityQueue.poll();
            assert node2 != null;
            NodeWithFreq parentNode = new NodeWithFreq(null, node1.getFreq() + node2.getFreq());
            Node internalNode = parentNode.getNode();
            internalNode.setLeft(node1.getNode());
            internalNode.setRight(node2.getNode());
            priorityQueue.offer(parentNode);
        }

        NodeWithFreq rootNode = priorityQueue.poll();
        assert rootNode != null;
        return new Tree(rootNode.getNode());
    }


    public static Map<Byte, Byte[]> buildCodeTable(Node rootNode) {
        HashMap<Byte, Byte[]> codeTable = new HashMap<>();
        if (rootNode != null) {
            buildCodeTableRecursive(rootNode,new Byte[]{0, 0}, codeTable);
        }
        return codeTable;
    }

    private static void buildCodeTableRecursive(Node node, Byte[] flag, Map<Byte, Byte[]> codeTable) {
        if (node.getData() != null) {
            Byte[] finalFlag = flag.clone();
            codeTable.put(node.getData(), finalFlag);
        } else {
            if (node.getLeft() != null) {

                Byte[] finalFlag = flag.clone();

                finalFlag[0] ++;
                finalFlag[1] = (byte) (finalFlag[1] << (byte) 1);
                buildCodeTableRecursive(node.getLeft(), finalFlag, codeTable);
            }
            if (node.getRight() != null) {
                Byte[] finalFlag = flag.clone();
                finalFlag[0] ++;
                finalFlag[1] = (byte) ((finalFlag[1] << (byte) 1) | (byte) 1);
                buildCodeTableRecursive(node.getRight(), finalFlag, codeTable);
            }
        }
    }
}
