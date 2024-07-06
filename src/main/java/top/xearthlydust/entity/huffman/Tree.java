package top.xearthlydust.entity.huffman;

import lombok.Data;
import top.xearthlydust.sign.ExceptionCodeEnum;
import top.xearthlydust.util.NodeException;

import java.util.Optional;

@Data
public class Tree {
    private Node root;

    public void GoLeft() throws NodeException {
        Optional<Node> left = Optional.ofNullable(this.root.getLeft());
        this.root.setLeft(left.orElseThrow(()->new NodeException(ExceptionCodeEnum.LEFT_NON_EXISTENT)));
    }
    public void GoRight() throws NodeException {
        Optional<Node> right = Optional.ofNullable(this.root.getRight());
        this.root.setRight(right.orElseThrow(()->new NodeException(ExceptionCodeEnum.RIGHT_NON_EXISTENT)));
    }

    public Tree(Node root) {
        this.root = root;
    }
}
