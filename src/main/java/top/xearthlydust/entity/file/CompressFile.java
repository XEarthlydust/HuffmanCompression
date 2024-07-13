package top.xearthlydust.entity.file;

import lombok.Data;
import top.xearthlydust.util.FileUtil;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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

    public CompressFile(List<File> fileList) {
        AtomicInteger id = new AtomicInteger(0);

        this.fileId = id.incrementAndGet();
        this.fileName = fileList.get(0).getAbsoluteFile().getParentFile().getName();
        this.isFolder = true;

        List<CompressFile> child = new ArrayList<>();
        fileList.forEach(file -> {
            if (file.isFile()) {
                child.add(new CompressFile(file.getName(), false, id.getAndIncrement()));
            } else if (file.isDirectory()) {
                CompressFile one = FileUtil.getDirectoryStructure(Paths.get(file.getAbsolutePath()), id);
                child.add(one);
            }
        });
        this.children = child;

    }
}
