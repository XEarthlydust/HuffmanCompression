import com.esotericsoftware.kryo.io.Input;
import org.junit.jupiter.api.Test;
import top.xearthlydust.entity.file.CompressFile;
import top.xearthlydust.entity.file.FileChunk;
import top.xearthlydust.entity.huffman.Node;
import top.xearthlydust.entity.huffman.Tree;
import top.xearthlydust.service.Compressor;
import top.xearthlydust.service.Decompressor;
import top.xearthlydust.util.FileUtil;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TestHuffman {
    @Test
    public void runHuffman() {
        String filePath = "D:\\a.jpg";
        String savePath = "D:\\tmp.dat";
        String decodePath =  "D:\\tmp.jpg";
        String decodePath2 =  "D:\\tmp2.jpg";

        CompressFile compressFile = new CompressFile("abc", false, 1);

        try  {
            Compressor.oneFileCompressWithSave(compressFile, filePath, savePath);
            try (FileInputStream fis = new FileInputStream(savePath);
                 Input input = new Input(fis)
            ) {
                while (input.available() > 0) {
                    FileChunk fileChunk = (FileChunk) FileUtil.deserializeOneObj(input);
                    Decompressor.oneSliceDecompressWithSave(fileChunk, decodePath);

                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void runNodeFrequencies() {

    }
}
