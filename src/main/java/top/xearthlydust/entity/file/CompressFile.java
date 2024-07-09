package top.xearthlydust.entity.file;

import lombok.Data;

import java.util.List;

@Data
public class CompressFile {
    private String fileName;
    private Integer fileId;
    private boolean isFolder;
    private List<CompressFile> children;

    public CompressFile(String fileName, boolean isFolder) {
        this.fileName = fileName;
        this.isFolder = isFolder;
    }
}
