import com.esotericsoftware.kryo.io.Input;
import org.junit.jupiter.api.Test;
import top.xearthlydust.entity.file.CompressFile;
import top.xearthlydust.entity.file.FileChunk;
import top.xearthlydust.entity.huffman.Node;
import top.xearthlydust.entity.huffman.Tree;
import top.xearthlydust.service.Compressor;
import top.xearthlydust.service.Decompressor;
import top.xearthlydust.service.ThreadPoolManager;
import top.xearthlydust.util.FileUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TestHuffman {
    @Test
    public void runHuffman() {
        String filePath = "E:\\UserData\\Info\\Account\\h.txt";
        String savePath = "E:\\a\\a.hfm";
        String decodePath =  "E:\\b";

        try {
            Compressor.finalCompressWithSave(filePath, savePath);


            CompressFile compressFile = null;
            try (FileInputStream fis = new FileInputStream(savePath);
                 Input input = new Input(fis)
            ) {
                compressFile = (CompressFile) FileUtil.deserializeOneObj(input);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Decompressor.finalDecompressWithSave(compressFile, savePath, decodePath);



        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void runNodeFrequencies() throws InterruptedException {

        String filePath = "E:\\UserData\\Info\\Account\\h.txt";
        String savePath = "E:\\a\\a.hfm";
        String decodePath =  "E:\\b\\h.txt";

        CompressFile compressFile = new CompressFile("h.txt", false, 1);
        Compressor.oneFileCompressWithSave(compressFile, filePath, savePath);


        try (FileInputStream fis = new FileInputStream(savePath);
             Input input = new Input(fis)
        ) {
//            FileUtil.deserializeOneObj(input);
            while (input.available() > 0) {
                final FileChunk fileChunk = (FileChunk) FileUtil.deserializeOneObj(input);
                Decompressor.oneSliceDecompressWithSave(fileChunk, decodePath);
                // 待测试
                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }




    }
}
