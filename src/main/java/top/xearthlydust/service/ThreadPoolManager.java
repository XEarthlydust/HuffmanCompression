package top.xearthlydust.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolManager {


    private static ExecutorService threadPool = Executors.newFixedThreadPool(16);

    public static void closeThreadPool() {
        threadPool.shutdown();
    }

    public static void restartThreadPool(int numThreads) {
        closeThreadPool();
        threadPool = Executors.newFixedThreadPool(numThreads);
    }

    public static void runOneTask(Runnable runnable) {
        threadPool.execute(runnable);
    }

    public static void closeAll(){
        threadPool.shutdownNow();
    }

    private ThreadPoolManager() {
    }
}
