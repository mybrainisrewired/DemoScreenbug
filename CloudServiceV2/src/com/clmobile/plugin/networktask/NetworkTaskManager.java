package com.clmobile.plugin.networktask;

import android.content.Context;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class NetworkTaskManager {
    private static NetworkTaskManager mInstance;
    private Context context;
    private ExecutorService mExecutor;

    static {
        mInstance = null;
    }

    private NetworkTaskManager(Context context) {
        this.context = context;
        this.mExecutor = Executors.newSingleThreadExecutor(new ThreadFactory() {
            public Thread newThread(Runnable runnable) {
                Thread thread = new Thread(runnable, "NetworkTaskManager Executor");
                thread.setDaemon(true);
                return thread;
            }
        });
    }

    public static synchronized NetworkTaskManager getInstance(Context context) {
        NetworkTaskManager networkTaskManager;
        synchronized (NetworkTaskManager.class) {
            if (mInstance == null) {
                mInstance = new NetworkTaskManager(context);
            }
            networkTaskManager = mInstance;
        }
        return networkTaskManager;
    }
}