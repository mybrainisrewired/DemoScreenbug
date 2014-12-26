package com.clmobile.plugin.task;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class AppDownLoadManage extends Thread {
    private static final long TIME_INTERVAL = 1000;
    private Context context;
    private ExecutorService mExecutor;
    private Object taskMutex;
    private List<AppDownLoadTask> tasks;

    public AppDownLoadManage(Context context) {
        this.tasks = new ArrayList();
        this.taskMutex = new Object();
        this.context = context;
        this.mExecutor = Executors.newSingleThreadExecutor(new ThreadFactory() {
            public Thread newThread(Runnable runnable) {
                Thread thread = new Thread(runnable, "DownLoadTaskManager Executor");
                thread.setDaemon(true);
                return thread;
            }
        });
    }

    public void addAppDownLoadTaskFromUrl(String url, String apkname, String packageName) {
    }

    public void run() {
        synchronized (this.taskMutex) {
        }
    }
}