package top.xearthlydust.entity.file.runtime;

import lombok.AllArgsConstructor;
import lombok.Data;
import top.xearthlydust.entity.file.FileSlice;

import java.util.Map;

@Data
@AllArgsConstructor
public class FileSliceWithTable implements Comparable<FileSliceWithTable> {
    FileSlice fileSlice;
    Map<Byte, String> codeTable;
    byte[] tmpByte;

    @Override
    public int compareTo(FileSliceWithTable o) {
        return this.fileSlice.compareTo(o.getFileSlice());
    }
}
