package com.google.android.gms.internal;

import android.os.Process;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public final class dp {
    private static final ThreadFactory ra;
    private static final ThreadPoolExecutor rb;

    static class AnonymousClass_1 implements Runnable {
        final /* synthetic */ Runnable rc;

        AnonymousClass_1(Runnable runnable) {
            this.rc = runnable;
        }

        public void run() {
            Process.setThreadPriority(ApiEventType.API_MRAID_USE_CUSTOM_CLOSE);
            this.rc.run();
        }
    }

    static {
        ra = new ThreadFactory() {
            private final AtomicInteger rd;

            {
                this.rd = new AtomicInteger(1);
            }

            public Thread newThread(Runnable runnable) {
                return new Thread(runnable, "AdWorker #" + this.rd.getAndIncrement());
            }
        };
        rb = new ThreadPoolExecutor(0, 10, 65, TimeUnit.SECONDS, new SynchronousQueue(true), ra);
    }

    public static void execute(Runnable task) {
        try {
            rb.execute(new AnonymousClass_1(task));
        } catch (RejectedExecutionException e) {
            dw.c("Too many background threads already running. Aborting task.  Current pool size: " + getPoolSize(), e);
        }
    }

    public static int getPoolSize() {
        return rb.getPoolSize();
    }
}