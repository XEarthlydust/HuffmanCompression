package top.xearthlydust.util;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import top.xearthlydust.entity.file.CompressFile;
import top.xearthlydust.entity.file.FileChunk;
import top.xearthlydust.service.KryoPoolManager;
import top.xearthlydust.service.TreeBuilder;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class FileUtil {

//    private static int fileIdCounter = 0;

    // 强制单线程 保障线程安全 按需顺序写入文件 对于无法确定大小的数据如此序列化
    public static void serializeOneObj(Object obj, String filePath) throws IOException {
        Kryo kryo = KryoPoolManager.singleUse();
        try (FileOutputStream fos = new FileOutputStream(filePath, true);
             Output output = new Output(fos)) {
            kryo.writeClassAndObject(output, obj);
            output.flush();
        } finally {
            KryoPoolManager.singleFree(kryo);
        }
    }

    // 可以多线程的 将解码后数据存回文件
    public static void saveOneChunk(FileChunk fileChunk, String filePath) {
        Path path = Paths.get(filePath);
        // 创建中间目录
        try {
            Files.createDirectories(path.getParent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (RandomAccessFile raf = new RandomAccessFile(filePath, "rw"); FileChannel channel = raf.getChannel()) {
            ByteBuffer buffer = ByteBuffer.wrap(fileChunk.getBytes());
            channel.position((long) (fileChunk.getId() - 1) * TreeBuilder.CHUNK_SIZE);
            while (buffer.hasRemaining()) {
                channel.write(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // 单线程地从序列化的文件流中读取一个对象
    public static Object deserializeOneObj(Input input) {
        Kryo kryo = KryoPoolManager.singleUse();
        Object oneObj = kryo.readClassAndObject(input);
        KryoPoolManager.singleFree(kryo);
        return oneObj;
    }

    public static CompressFile getDirectoryStructure(Path path, AtomicInteger id) {

        // 创建根目录的CompressFile对象

        CompressFile root = new CompressFile(path.getFileName().toString(), Files.isDirectory(path), id.getAndIncrement());

        if (root.isFolder()) {
            List<CompressFile> children = new ArrayList<>();
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
                for (Path entry : stream) {
                    // 递归调用，获取子文件或目录的CompressFile对象
                    children.add(getDirectoryStructure(entry, id));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            root.setChildren(children);
        }
        return root;
    }

}
