package com.example.livedatademo.utils;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;

/**
 * 使用方法如下：
 * UI 线程
 * AndPoolExecutors.getInstance().mainThread().execute(new Runnable() {
 *
 * @Override public void run() {
 * //do something
 * }
 * });
 * 磁盘IO线程池
 * AndPoolExecutors.getInstance().diskIO().execute(new Runnable() {
 * @Override public void run() {
 * //do something
 * }
 * });
 * 网络IO线程池
 * AndPoolExecutors.getInstance().networkIO().execute(new Runnable() {
 * @Override public void run() {
 * //do something
 * }
 * });
 * 定时(延时)任务线程池
 * 延时3秒后执行：
 * AndPoolExecutors.getInstance().scheduledExecutor().schedule(new Runnable() {
 * @Override public void run() {
 * // do something
 * }
 * },3,TimeUnit.SECONDS);
 * 5秒后启动第一次,每3秒执行一次(第一次开始执行和第二次开始执行之间间隔3秒)
 * AndPoolExecutors.getInstance().scheduledExecutor().scheduleAtFixedRate(new Runnable() {
 * @Override public void run() {
 * // do something
 * }
 * }, 5, 3, TimeUnit.MILLISECONDS);
 * <p>
 * 5秒后启动第一次,每3秒执行一次(第一次执行完成和第二次开始之间间隔3秒)
 * AndPoolExecutors.getInstance().scheduledExecutor().scheduleWithFixedDelay(new Runnable() {
 * @Override public void run() {
 * // do something
 * }
 * }, 5, 3, TimeUnit.MILLISECONDS);
 * 取消定时器(等待当前任务结束后，取消定时器)
 * scheduledFuture.cancel(false);
 * 取消定时器(不等待当前任务结束，取消定时器)
 * scheduledFuture.cancel(true);
 *
 * @author zhutiankang
 */
public final class AndPoolExecutors {
    private static final String TAG = "AppExecutors";
    /**
     * 磁盘IO线程池
     **/
    private final ExecutorService diskIO;
    /**
     * 网络IO线程池
     **/
    private final ExecutorService networkIO;
    /**
     * UI线程
     **/
    private final Executor mainThread;
    /**
     * 定时任务线程池
     **/
    private final ScheduledExecutorService scheduledExecutor;

//    private volatile static AndPoolExecutors andPoolExecutors;

    public static AndPoolExecutors getInstance() {
        return Holder.AND_POOL_EXECUTORS;
//        if (andPoolExecutors == null) {
//            synchronized (AndPoolExecutors.class) {
//                if (andPoolExecutors == null) {
//                    andPoolExecutors = new AndPoolExecutors();
//                }
//            }
//        }
//        return andPoolExecutors;
    }

    private final static class Holder {
        private final static AndPoolExecutors AND_POOL_EXECUTORS = new AndPoolExecutors();
    }

    private AndPoolExecutors(ExecutorService diskIO, ExecutorService networkIO, Executor mainThread, ScheduledExecutorService scheduledExecutor) {
        this.diskIO = diskIO;
        this.networkIO = networkIO;
        this.mainThread = mainThread;
        this.scheduledExecutor = scheduledExecutor;
    }

    private AndPoolExecutors() {
        this(diskIoExecutor(), networkExecutor(), new MainThreadExecutor(), scheduledThreadPoolExecutor());
    }

    /**
     * 定时(延时)任务线程池
     * <p>
     * 替代Timer,执行定时任务,延时任务
     */
    public ScheduledExecutorService scheduledExecutor() {
        return scheduledExecutor;
    }

    /**
     * 磁盘IO线程池（单线程）
     * <p>
     * 和磁盘操作有关的进行使用此线程(如读写数据库,读写文件)
     * 禁止延迟,避免等待
     * 此线程不用考虑同步问题
     */
    public ExecutorService diskIO() {
        return diskIO;
    }

    /**
     * 网络IO线程池
     * <p>
     * 网络请求,异步任务等适用此线程
     * 不建议在这个线程 sleep 或者 wait
     */
    public ExecutorService networkIO() {
        return networkIO;
    }

    /**
     * UI线程
     * <p>
     * Android 的MainThread
     * UI线程不能做的事情这个都不能做
     */
    public Executor mainThread() {
        return mainThread;
    }

    private static ScheduledExecutorService scheduledThreadPoolExecutor() {
        return new ScheduledThreadPoolExecutor(16, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "scheduled_executor");
            }
        }, new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                Log.e(TAG, "rejectedExecution: scheduled executor queue overflow");
            }
        });
    }

    private static ExecutorService diskIoExecutor() {
        return new ThreadPoolExecutor(8, 12, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(1024), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "disk_executor");
            }
        }, new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                Log.e(TAG, "rejectedExecution: disk io executor queue overflow");
            }
        });
    }

    private static ExecutorService networkExecutor() {
        return new ThreadPoolExecutor(6, 9, 1000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(6), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "network_executor");
            }
        }, new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                Log.e(TAG, "rejectedExecution: network executor queue overflow");
            }
        });
    }


    private static class MainThreadExecutor implements Executor {
        private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
