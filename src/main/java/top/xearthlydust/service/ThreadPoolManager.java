package top.xearthlydust.service;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.util.Pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolManager {


    private static ExecutorService threadPool = Executors.newFixedThreadPool(16);

    public static void closeThreadPool() {
        threadPool.shutdown();
    }

    public static void restartThreadPool(int numThreads) {
        if (numThreads < 1) {
            return;
        } else if (numThreads > 256) {
            return;
        } else {
            closeThreadPool();
            threadPool = Executors.newFixedThreadPool(numThreads);
        }
    }

    public static void runOneTask(Runnable runnable) {
        threadPool.execute(runnable);
    }

    public static boolean isTerminated() {
        return threadPool.isTerminated();
    }

    public static void closeAll(){
        threadPool.shutdownNow();
    }

    private ThreadPoolManager() {
    }
}
