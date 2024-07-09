package top.xearthlydust.entity.file.runtime;

import lombok.AllArgsConstructor;
import lombok.Data;
import top.xearthlydust.entity.file.FileChunk;

import java.util.Map;

@Data
@AllArgsConstructor
public class FileSliceWithTable implements Comparable<FileSliceWithTable> {
    FileChunk fileChunk;
    Map<Byte, String> codeTable;
    byte[] tmpByte;

    @Override
    public int compareTo(FileSliceWithTable o) {
        return this.fileChunk.compareTo(o.getFileChunk());
    }
}
