package top.xearthlydust.entity.huffman.runtime;

import lombok.Data;
import top.xearthlydust.entity.huffman.Node;

@Data
public class NodeWithFreq implements Comparable<NodeWithFreq> {

    private final Node node;
    private final Integer freq;

    public NodeWithFreq(Byte data,Integer freq) {
        this.node = new Node(data);
        this.freq = freq;
    }

    @Override
    public String toString() {
        return freq.toString();
    }

    @Override
    public int compareTo(NodeWithFreq o) {
        return Integer.compare(this.freq, o.freq);
    }
}
