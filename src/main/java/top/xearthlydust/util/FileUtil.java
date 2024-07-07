package top.xearthlydust.util;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import top.xearthlydust.entity.file.FileSlice;
import top.xearthlydust.entity.huffman.Tree;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    private static final ThreadLocal<Kryo> kryoLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
//        kryo.setRegistrationRequired(false);
//        kryo.setReferences(false);
        kryo.register(FileSlice.class);
        kryo.register(Tree.class);
        kryo.register(byte[].class);
        kryo.register(top.xearthlydust.entity.huffman.Node.class);
        return kryo;
    });

    public static FileSlice readOneSlice(FileInputStream fis) {
        Input input = new Input(fis);
        return kryoLocal.get().readObject(input, FileSlice.class);
    }

    // 序列化对象并写入文件
    public static void serializeObject(List<FileSlice> objects, String filePath) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(filePath, "rw"); FileChannel fileChannel = raf.getChannel()) {
            for (FileSlice obj : objects) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                Output output = new Output(byteArrayOutputStream);
                obj.getTree().clearMap();
                kryoLocal.get().writeObject(output, obj);
                output.flush();
                byte[] data = byteArrayOutputStream.toByteArray();

                // 记录对象大小
                int objectSize = data.length;
                MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, fileChannel.size(), 4 + objectSize);
                buffer.putInt(objectSize);
                buffer.put(data);
            }
        }
    }

    // 从文件反序列化对象
    public static List<FileSlice> deserializeObjects(String filePath) throws IOException {
        List<FileSlice> objects = new ArrayList<>();

        try (RandomAccessFile raf = new RandomAccessFile(filePath, "r"); FileChannel fileChannel = raf.getChannel()) {
            long fileSize = fileChannel.size();
            MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileSize);

            while (buffer.hasRemaining()) {
                // 读取对象大小
                int objectSize = buffer.getInt();
                byte[] objectBytes = new byte[objectSize];
                buffer.get(objectBytes);

                Input input = new Input(objectBytes);
                FileSlice obj = kryoLocal.get().readObject(input, FileSlice.class);
                objects.add(obj);
            }
        }

        return objects;
    }
}
