package com.hyaline.avoidbrowser.utils;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Wang.Wenhui
 * Date: 2020/5/19
 */
public class ThreadPool {
    private static ThreadPool instance;
    private static final Object lock = new Object();

    private final ExecutorService single;
    private final ExecutorService fixed;
    private final ExecutorService cache;
    private final ScheduledExecutorService schedule;
    private final Executor main;

    private ThreadPool() {
        single = Executors.newSingleThreadExecutor(new MyTreadFactory("single"));
        fixed = Executors.newFixedThreadPool(5, new MyTreadFactory("fixed"));
        cache = Executors.newCachedThreadPool(new MyTreadFactory("cache"));
        schedule = new ScheduledThreadPoolExecutor(3, new MyTreadFactory("schedule"), new ThreadPoolExecutor.AbortPolicy());
        main = new MainThreadExecutor();
    }

    public static ThreadPool instance() {
        synchronized (lock) {
            if (instance == null) {
                instance = new ThreadPool();
            }
        }
        return instance;
    }

    /**
     * 单线程，每次执行只存在一个线程
     *
     * @return Executor
     */
    public static ExecutorService single() {
        return instance().single;
    }

    /**
     * 缓存池，适用于短且频繁的异步任务
     *
     * @return Executor
     */
    public static ExecutorService cache() {
        return instance().cache;
    }

    /**
     * 执行时先判断是否在主线程，如果是则开启线程执行,否则直接执行
     *
     * @param runnable 执行体
     */
    public static void runInThreadIfNeed(Runnable runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            cache().execute(runnable);
        } else {
            runnable.run();
        }
    }

    /**
     * 固定线程数量线程池
     *
     * @return Executor
     */
    public static ExecutorService fixed() {
        return instance().fixed;
    }

    /**
     * 主线程
     *
     * @return Executor
     */
    public static Executor main() {
        return instance().main;
    }

    /**
     * 可执行定时功能的线程池
     *
     * @return Executor
     */
    public static ScheduledExecutorService schedule() {
        return instance().schedule;
    }

    private class MyTreadFactory implements ThreadFactory {
        private final String poolName;
        private long count;

        MyTreadFactory(String poolName) {
            this.poolName = poolName;
            count = 0;
        }

        @Override
        public Thread newThread(@NonNull Runnable r) {
            return new Thread(r, "wht-" + poolName + "-" + (++count));
        }
    }

    private static class MainThreadExecutor implements Executor {
        private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
