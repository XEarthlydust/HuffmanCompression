package top.xearthlydust.service;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.util.Pool;
import top.xearthlydust.entity.file.FileChunk;
import top.xearthlydust.entity.huffman.Tree;

public class KryoPoolManager {

    private static final int MAX_POOL_SIZE = 8;

    // 注册配置
    private static Kryo registerKryo(Kryo kryo) {
        kryo.register(FileChunk.class);
        kryo.register(Tree.class);
        kryo.register(byte[].class);
        kryo.register(top.xearthlydust.entity.huffman.Node.class);
        return kryo;
    }

    // 可以同时拿多个线程安全的kryo实例
    private static final Pool<Kryo> kryoPool = new Pool<Kryo>(true, false, MAX_POOL_SIZE) {
        @Override
        protected Kryo create() {
            return registerKryo(new Kryo());
        }
    };

    // 从池拿一个kryo对象
    public static Kryo borrow() {
        return kryoPool.obtain();
    }

    // 将一个kryo对象归还释放
    public static void release(Kryo kryo) {
        kryoPool.free(kryo);
    }

    // 用于有顺序相关、线程同步的kryo实例
    private static final Pool<Kryo> singleKryo = new Pool<Kryo>(true, false, 1) {
        @Override
        protected Kryo create() {
            return registerKryo(new Kryo());
        }
    };

    public static Kryo singleUse() {
        return singleKryo.obtain();
    }

    public static void singleFree(Kryo kryo) {
        singleKryo.free(kryo);
    }
}