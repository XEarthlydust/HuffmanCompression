package top.xearthlydust.entity.file;

import lombok.Data;
import top.xearthlydust.entity.huffman.Tree;

@Data
public class FileChunk implements Comparable<FileChunk> {
    private Integer id;
    private Tree tree;
    private byte[] bytes;

    @Override
    public int compareTo(FileChunk o) {
        return this.id - o.id;
    }

    public FileChunk(Integer id, Tree tree) {
        this.id = id;
        this.tree = tree;
    }
}
