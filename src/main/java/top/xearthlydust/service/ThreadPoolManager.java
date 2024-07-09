package top.xearthlydust.service;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.util.Pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolManager {


    private static final ExecutorService threadPool = Executors.newFixedThreadPool(16);

    public static void closeThreadPool() {
        threadPool.shutdown();
    }

    public static void runOneTask(Runnable runnable) {
        threadPool.execute(runnable);
    }

    public static boolean isTerminated() {
        return threadPool.isTerminated();
    }

    private ThreadPoolManager() {
    }
}
