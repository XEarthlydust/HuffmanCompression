package top.xearthlydust.util;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import top.xearthlydust.entity.file.FileChunk;
import top.xearthlydust.service.KryoPoolManager;
import top.xearthlydust.service.TreeBuilder;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileUtil {



    // 强制单线程 保障线程安全 按需顺序写入文件 对于无法确定大小的数据如此序列化
    public static void serializeOneObj(Object obj, String filePath) throws IOException {
        Kryo kryo = KryoPoolManager.singleUse();
        try (FileOutputStream fos = new FileOutputStream(filePath, true);
             Output output = new Output(fos)) {
            kryo.writeObject(output, obj);
            output.flush();
        } finally {
            KryoPoolManager.singleFree(kryo);
        }
    }

    // 可以多线程的 将解码后数据存回文件
    public static void saveOneChunk(FileChunk fileChunk, String filePath) {
        try (RandomAccessFile raf = new RandomAccessFile(filePath, "rw"); FileChannel channel = raf.getChannel()) {
            ByteBuffer buffer = ByteBuffer.wrap(fileChunk.getBytes());
            channel.position((long) (fileChunk.getId() - 1) * TreeBuilder.CHUNK_SIZE);
            while (buffer.hasRemaining()) {
                channel.write(buffer);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    // 单线程地从序列化的文件流中读取一个对象
//    public static Object deserializeOneObj(Input input) {
//        Kryo kryo = KryoPoolManager.singleUse();
//        Object oneObj = kryo.readClassAndObject(input);
//        KryoPoolManager.singleFree(kryo);
//        return oneObj;
//    }
    public static <T> T deserializeOneObj(Input input, Class<T> clazz) {
        Kryo kryo = KryoPoolManager.singleUse();
        T oneObj = kryo.readObject(input, clazz);
        KryoPoolManager.singleFree(kryo);
        return oneObj;
    }

}
