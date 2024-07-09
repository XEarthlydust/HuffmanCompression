package top.xearthlydust.entity.huffman;

import lombok.Data;
import top.xearthlydust.enums.ExceptionCodeEnum;
import top.xearthlydust.util.NodeException;

import java.util.Map;
import java.util.Optional;

@Data
public class Tree {
    private Node root;
    private Map<Byte, Byte[]> codeTable;

    public Node GoLeft() throws NodeException {
        Optional<Node> left = Optional.ofNullable(this.root.getLeft());
        return left.orElseThrow(() -> new NodeException(ExceptionCodeEnum.LEFT_NON_EXISTENT));
    }

    public Node GoRight() throws NodeException {
        Optional<Node> right = Optional.ofNullable(this.root.getRight());
        return right.orElseThrow(() -> new NodeException(ExceptionCodeEnum.RIGHT_NON_EXISTENT));
    }

    public Tree(Node root) {
        this.root = root;
    }

    public void clearMap() {
        this.codeTable = null;
    }
}
