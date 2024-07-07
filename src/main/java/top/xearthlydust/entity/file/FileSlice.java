package top.xearthlydust.entity.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import top.xearthlydust.entity.huffman.Tree;

@Data
public class FileSlice implements Comparable<FileSlice> {
    private Integer id;
    private Tree tree;
    private byte[] bytes;

    @Override
    public int compareTo(FileSlice o) {
        return this.id - o.id;
    }

    public FileSlice(Integer id, Tree tree) {
        this.id = id;
        this.tree = tree;
    }
}
