import org.junit.jupiter.api.Test;
import top.xearthlydust.entity.file.CompressFile;
import top.xearthlydust.entity.file.runtime.FileSliceWithTable;
import top.xearthlydust.entity.huffman.runtime.NodeWithFreq;
import top.xearthlydust.entity.huffman.Tree;
import top.xearthlydust.service.TreeBuilder;
import top.xearthlydust.util.HuffmanUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class TestHuffman {
    @Test
    public void runHuffman() throws InterruptedException {
        String filename = "E:\\UserData\\Learn\\JavaLearn\\HuffmanCompression\\src\\test\\resources\\a.jpg";
        try (FileInputStream fis = new FileInputStream(filename)) {
            System.out.println("This is one line");

            List<Tree> a = TreeBuilder.buildFileTree(new CompressFile(filename));
            for (Tree tree : a) {
                System.out.println(tree.getCodeTable().get((byte) 126)[0].toString() +"|"+ tree.getCodeTable().get((byte) 126)[1].toString());
                System.out.println(tree.getCodeTable().get((byte) -127)[0].toString() +"|"+ tree.getCodeTable().get((byte) -127)[1].toString());
                System.out.println(tree.getCodeTable().get((byte) 127)[0].toString() +"|"+ tree.getCodeTable().get((byte) 127)[1].toString());
            }
        } catch (IOException e) {
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
