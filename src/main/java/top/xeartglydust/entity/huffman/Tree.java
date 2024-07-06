package top.xeartglydust.entity.huffman;

import lombok.AllArgsConstructor;
import lombok.Data;
import top.xeartglydust.sign.ExceptionCodeEnum;
import top.xeartglydust.util.exception.NodeException;

import java.util.HashMap;
import java.util.Optional;

@Data
@AllArgsConstructor
public class Tree {
    private Node root;
    private HashMap<Integer, Node> nodes;

    public void GoLeft() throws NodeException {
        Optional<Node> left = Optional.ofNullable(this.root.getLeft());
        this.root.setLeft(left.orElseThrow(()->new NodeException(ExceptionCodeEnum.LEFT_NON_EXISTENT)));
    }
    public void GoRight() throws NodeException {
        Optional<Node> right = Optional.ofNullable(this.root.getRight());
        this.root.setRight(right.orElseThrow(()->new NodeException(ExceptionCodeEnum.RIGHT_NON_EXISTENT)));
    }

}
