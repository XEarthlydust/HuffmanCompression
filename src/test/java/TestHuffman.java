import org.junit.jupiter.api.Test;
import top.xearthlydust.entity.huffman.NodeWithFreq;
import top.xearthlydust.entity.huffman.Tree;
import top.xearthlydust.service.TreeBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;


public class TestHuffman {
    @Test
    public void runHuffman() throws InterruptedException {
        String filename = "E:\\UserData\\Learn\\JavaLearn\\HuffmanCompression\\src\\test\\resources\\a.jpg";
        try (FileInputStream fis = new FileInputStream(filename)) {
            System.out.println("This is one line");
            Map<Tree, Map<Byte, String>> treeMap = TreeBuilder.buildTree(fis);
            System.out.println(treeMap);

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
