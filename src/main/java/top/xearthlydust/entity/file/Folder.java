package top.xearthlydust.entity.file;

import lombok.Data;

import java.io.File;
import java.util.List;

@Data
public class Folder {
    private List<Folder> childFolders;
    private List<File> childFiles;
}
