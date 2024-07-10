package top.xearthlydust.entity.file;

import lombok.Data;

import java.util.List;

@Data
public class CompressFile {
    private String fileName;
    private int fileId;
    private boolean isFolder;
    private List<CompressFile> children;

    public CompressFile(String fileName, boolean isFolder,int fileId) {
        this.fileName = fileName;
        this.isFolder = isFolder;
        this.fileId = fileId;
    }
}
