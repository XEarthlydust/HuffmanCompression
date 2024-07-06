package top.xearthlydust.entity.huffman;

import lombok.Data;

@Data
public class Node {
    private Node left;
    private Node right;
    private Byte data;
    public Node(Byte data) {
        this.data = data;
    }
}
