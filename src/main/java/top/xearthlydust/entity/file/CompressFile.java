package top.xearthlydust.entity.file;

import lombok.Data;

import java.util.List;

@Data
public class CompressFile {
    private String fileName;
    private List<FileSlice> fileSlices;

    public CompressFile(String fileName) {
        this.fileName = fileName;
    }
}
