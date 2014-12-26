package com.wmt.data.utils;

import android.util.Log;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPool {
    private static final int CORE_POOL_SIZE = 4;
    public static final JobContext JOB_CONTEXT_STUB;
    private static final int KEEP_ALIVE_TIME = 10;
    private static final int MAX_POOL_SIZE = 8;
    public static final int MODE_CPU = 1;
    public static final int MODE_NETWORK = 2;
    public static final int MODE_NONE = 0;
    private static final String TAG = "ThreadPool";
    ResourceCounter mCpuCounter;
    private final Executor mExecutor;
    ResourceCounter mNetworkCounter;

    public static interface CancelListener {
        void onCancel();
    }

    public static interface Job<T> {
        T run(com.wmt.data.utils.ThreadPool.JobContext jobContext);
    }

    public static interface JobContext {
        boolean isCancelled();

        void setCancelListener(com.wmt.data.utils.ThreadPool.CancelListener cancelListener);

        boolean setMode(int i);
    }

    private static class ResourceCounter {
        public int value;

        public ResourceCounter(int v) {
            this.value = v;
        }
    }

    private static class JobContextStub implements com.wmt.data.utils.ThreadPool.JobContext {
        private JobContextStub() {
        }

        public boolean isCancelled() {
            return false;
        }

        public void setCancelListener(com.wmt.data.utils.ThreadPool.CancelListener listener) {
        }

        public boolean setMode(int mode) {
            return true;
        }
    }

    private class Worker<T> implements Runnable, Future<T>, com.wmt.data.utils.ThreadPool.JobContext {
        private static final String TAG = "Worker";
        private com.wmt.data.utils.ThreadPool.CancelListener mCancelListener;
        private volatile boolean mIsCancelled;
        private boolean mIsDone;
        private com.wmt.data.utils.ThreadPool.Job<T> mJob;
        private FutureListener<T> mListener;
        private int mMode;
        private T mResult;
        private ResourceCounter mWaitOnResource;

        public Worker(com.wmt.data.utils.ThreadPool.Job<T> job, FutureListener<T> listener) {
            this.mJob = job;
            this.mListener = listener;
        }

        private boolean acquireResource(ResourceCounter counter) {
            while (true) {
                synchronized (this) {
                    if (this.mIsCancelled) {
                        this.mWaitOnResource = null;
                        return false;
                    } else {
                        this.mWaitOnResource = counter;
                        synchronized (counter) {
                            if (counter.value > 0) {
                                counter.value--;
                                synchronized (this) {
                                    this.mWaitOnResource = null;
                                }
                                return true;
                            } else {
                                try {
                                    counter.wait();
                                } catch (InterruptedException e) {
                                }
                            }
                        }
                    }
                }
            }
        }

        private ResourceCounter modeToCounter(int mode) {
            if (mode == 1) {
                return ThreadPool.this.mCpuCounter;
            }
            return mode == 2 ? ThreadPool.this.mNetworkCounter : null;
        }

        private void releaseResource(ResourceCounter counter) {
            synchronized (counter) {
                counter.value++;
                counter.notifyAll();
            }
        }

        public synchronized void cancel() {
            try {
                if (!this.mIsCancelled) {
                    this.mIsCancelled = true;
                    if (this.mWaitOnResource != null) {
                        synchronized (this.mWaitOnResource) {
                            this.mWaitOnResource.notifyAll();
                        }
                    }
                    if (this.mCancelListener != null) {
                        this.mCancelListener.onCancel();
                    }
                }
            } catch (Throwable th) {
            }
        }

        public synchronized T get() {
            while (!this.mIsDone) {
                try {
                    wait();
                } catch (Exception e) {
                    Log.w(TAG, "ingore exception", e);
                }
            }
            return this.mResult;
        }

        public boolean isCancelled() {
            return this.mIsCancelled;
        }

        public synchronized boolean isDone() {
            return this.mIsDone;
        }

        public void run() {
            T result = null;
            if (setMode(MODE_CPU)) {
                try {
                    result = this.mJob.run(this);
                } catch (Throwable th) {
                    Log.w(TAG, "Exception in running a job", th);
                }
            }
            synchronized (this) {
                setMode(MODE_NONE);
                this.mResult = result;
                this.mIsDone = true;
                notifyAll();
            }
            if (this.mListener != null) {
                this.mListener.onFutureDone(this);
            }
        }

        public synchronized void setCancelListener(com.wmt.data.utils.ThreadPool.CancelListener listener) {
            this.mCancelListener = listener;
            if (this.mIsCancelled && this.mCancelListener != null) {
                this.mCancelListener.onCancel();
            }
        }

        public boolean setMode(int mode) {
            ResourceCounter rc = modeToCounter(this.mMode);
            if (rc != null) {
                releaseResource(rc);
            }
            this.mMode = 0;
            rc = modeToCounter(mode);
            if (rc != null && !acquireResource(rc)) {
                return false;
            }
            this.mMode = mode;
            return true;
        }

        public void waitDone() {
            get();
        }
    }

    static {
        JOB_CONTEXT_STUB = new JobContextStub();
    }

    public ThreadPool() {
        this.mCpuCounter = new ResourceCounter(2);
        this.mNetworkCounter = new ResourceCounter(2);
        this.mExecutor = new ThreadPoolExecutor(4, 8, 10, TimeUnit.SECONDS, new LinkedBlockingQueue(), new PriorityThreadFactory("thread-pool", 10));
    }

    public <T> Future<T> submit(Job<T> job) {
        return submit(job, null);
    }

    public <T> Future<T> submit(Job<T> job, FutureListener<T> listener) {
        Worker<T> w = new Worker(job, listener);
        this.mExecutor.execute(w);
        return w;
    }
}