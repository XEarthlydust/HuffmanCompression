import org.junit.jupiter.api.Test;
import top.xearthlydust.entity.file.FileSlice;
import top.xearthlydust.entity.huffman.runtime.NodeWithFreq;
import top.xearthlydust.service.Compressor;
import top.xearthlydust.util.FileUtil;

import java.util.List;


public class TestHuffman {
    @Test
    public void runHuffman() {
        String filename = "E:\\UserData\\Learn\\JavaLearn\\HuffmanCompression\\src\\test\\resources\\out2.mp4";
        String saveName = "E:\\UserData\\Learn\\JavaLearn\\HuffmanCompression\\src\\test\\resources\\tmp.dat";
        try {
            List<FileSlice> list = Compressor.oneFileCompress(filename, filename);
            FileUtil.serializeObject(list, saveName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void runNodeFrequencies() {
        NodeWithFreq O1 = new NodeWithFreq(Byte.valueOf("0"), 1);
        NodeWithFreq O2 = new NodeWithFreq(Byte.valueOf("1"), 2);
        Integer a = 1;
        Integer b = 2;
        System.out.println(O1.compareTo(O2));
        System.out.println(a.compareTo(b));
    }
}
