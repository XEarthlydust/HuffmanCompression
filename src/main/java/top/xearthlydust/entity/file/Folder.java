package top.xearthlydust.entity.file;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Folder {
    private String folderName;
    private List<Folder> childFolders;
    private List<CompressFile> childFiles;
    private Boolean isRoot;
}
