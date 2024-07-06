package top.xearthlydust.util;

import top.xearthlydust.entity.huffman.NodeWithFreq;
import top.xearthlydust.entity.huffman.Tree;
import top.xearthlydust.entity.huffman.Node;

import java.util.*;

public class HuffmanUtil {
    public static PriorityQueue<NodeWithFreq> checkStreamToMap(byte[] bytes, Integer num) throws NodeException {
        HashMap<Byte, Integer> frequencyMap = new HashMap<>();

        for (int i = 0; i < num; i++) {
            frequencyMap.merge(bytes[i], 1, Integer::sum);
        }
        
//        for (Byte aByte : bytes) {
//            frequencyMap.merge(aByte, 1, Integer::sum);
//        }

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


    public static Map<Byte, String> buildCodeTable(Node rootNode) {
        HashMap<Byte, String> codeTable = new HashMap<>();
        if (rootNode != null) {
            buildCodeTableRecursive(rootNode, "", codeTable);
        }
        return codeTable;
    }

    public static void buildCodeTableRecursive(Node node, String code, Map<Byte, String> codeTable) {
        if (node.getData() != null) {
            codeTable.put(node.getData(), code);
        } else {
            if (node.getLeft() != null) {
                buildCodeTableRecursive(node.getLeft(), code + "0", codeTable);
            }
            if (node.getRight() != null) {
                buildCodeTableRecursive(node.getRight(), code + "1", codeTable);
            }
        }
    }
}
