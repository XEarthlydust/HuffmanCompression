package top.xearthlydust.entity.file;

import lombok.Data;
import top.xearthlydust.entity.huffman.Tree;

@Data
public class FileSlice {
    private Integer id;
    private Tree tree;
}
