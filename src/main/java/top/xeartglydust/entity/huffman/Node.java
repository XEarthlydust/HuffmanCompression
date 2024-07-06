package top.xeartglydust.entity.huffman;

import lombok.Data;

@Data
public class Node {
    private Node left;
    private Node right;
    private NodeData data;
    public Node(NodeData data) {
        this.data = data;
    }
}
