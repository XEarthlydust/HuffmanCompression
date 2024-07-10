package top.xearthlydust.entity.huffman;

import lombok.Data;

@Data
public class Node implements Comparable<Node> {
    private Node left;
    private Node right;
    private byte data;
    private transient int freq;

    @Override
    public int compareTo(Node o) {
        return Integer.compare(this.freq, o.freq);
    }

    public Node(Byte data) {
        this.data = data;
    }
    public Node() {}
}
