package top.xearthlydust.entity.huffman;

import lombok.Data;

import java.util.Map;

@Data
public class Tree {
    private Node root;
    private transient Map<Byte, Byte[]> codeTable; // transient 标记不需要序列化

    public Tree(Node root) {
        this.root = root;
    }

    public void clearMap() {
        this.codeTable = null;
    }
}
