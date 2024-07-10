package top.xearthlydust.service;

import top.xearthlydust.entity.file.FileChunk;
import top.xearthlydust.util.FileUtil;
import top.xearthlydust.util.HuffmanUtil;


public class Decompressor {

    public static void oneSliceDecompressWithSave(FileChunk fileChunk, String savePath) {
        fileChunk.setBytes(HuffmanUtil.decodeBytes(
                fileChunk.getTree(),
                fileChunk.getBytes(),
                fileChunk.getLength()
        ));
        FileUtil.saveOneChunk(fileChunk, savePath);
    }
}
