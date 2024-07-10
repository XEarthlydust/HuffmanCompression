package top.xearthlydust.entity.file;

import lombok.Data;
import top.xearthlydust.entity.huffman.Tree;

@Data
public class FileChunk implements Comparable<FileChunk> {
    private int id;
    private int fileId; // 不可避免使用基类 这个属性是被CompressFile赋值 序列化时可能会被引用导致循环引用
    private int length;
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
