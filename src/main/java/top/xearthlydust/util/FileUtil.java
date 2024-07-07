package top.xearthlydust.util;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import top.xearthlydust.entity.file.CompressFile;
import top.xearthlydust.entity.file.FileSlice;
import top.xearthlydust.entity.file.Folder;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class FileUtil {

    private static final ThreadLocal<Kryo> kryoLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.setRegistrationRequired(false);
        kryo.setReferences(false);
        kryo.register(Folder.class);
        kryo.register(CompressFile.class);
        kryo.register(FileSlice.class);
        return kryo;
    });


    public static void saveBitFile(CompressFile compressFile, FileOutputStream fos) {
        Output output = new Output(fos);
        kryoLocal.get().writeObject(output, compressFile);
    }

    public static FileSlice readOneSlice(FileInputStream fis) {
        Input input = new Input(fis);
        return kryoLocal.get().readObject(input, FileSlice.class);
    }

    public static void readProject(Folder folder, FileInputStream fis) {

    }
}
