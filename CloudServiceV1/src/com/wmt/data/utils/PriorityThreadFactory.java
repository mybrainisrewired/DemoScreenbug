package com.wmt.data.utils;

import android.os.Process;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class PriorityThreadFactory implements ThreadFactory {
    private final String mName;
    private final AtomicInteger mNumber;
    private final int mPriority;

    class AnonymousClass_1 extends Thread {
        AnonymousClass_1(Runnable x0, String x1) {
            super(x0, x1);
        }

        public void run() {
            Process.setThreadPriority(PriorityThreadFactory.this.mPriority);
            super.run();
        }
    }

    public PriorityThreadFactory(String name, int priority) {
        this.mNumber = new AtomicInteger();
        this.mName = name;
        this.mPriority = priority;
    }

    public Thread newThread(Runnable r) {
        return new AnonymousClass_1(r, this.mName + '-' + this.mNumber.getAndIncrement());
    }
}