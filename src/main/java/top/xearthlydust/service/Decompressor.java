package top.xearthlydust.service;

import top.xearthlydust.entity.file.FileChunk;
import top.xearthlydust.util.BitUtil;


public class Decompressor {

    public static void oneSliceDecompressWithSave(FileChunk fileChunk) throws InterruptedException {
        ThreadPoolManager.runOneTask(() -> {
            fileChunk.setBytes(BitUtil.getCodeByTree(fileChunk.getTree(), fileChunk.getBytes()));
        });

    }
}
