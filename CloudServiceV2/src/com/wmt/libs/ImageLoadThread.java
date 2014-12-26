package com.wmt.libs;

import com.wmt.util.Deque;

public class ImageLoadThread extends Thread {
    private int mDequeSize;
    private boolean mExitMark;
    Object mObject;
    private Deque<Object> mRequestLoadQueue;

    public ImageLoadThread() {
        super("ImageLoad");
        this.mExitMark = false;
        this.mRequestLoadQueue = new Deque();
    }

    private void guardedRun() throws Exception {
        while (!this.mExitMark) {
            synchronized (this.mRequestLoadQueue) {
                this.mObject = this.mRequestLoadQueue.pollFirst();
                if (this.mObject == null) {
                    this.mRequestLoadQueue.wait();
                }
            }
            if (!this.mExitMark) {
                if (this.mObject != null) {
                    loadThumbBitmap(this.mObject);
                }
            } else {
                return;
            }
        }
    }

    public void exit() {
        synchronized (this.mRequestLoadQueue) {
            this.mExitMark = true;
            this.mRequestLoadQueue.clear();
            this.mRequestLoadQueue.notify();
        }
    }

    protected void loadThumbBitmap(Object object) {
    }

    public Object requestLoadQueue(Object object) {
        Object obj = null;
        synchronized (this.mRequestLoadQueue) {
            this.mRequestLoadQueue.addFirst(object);
            if (this.mDequeSize > 0 && this.mRequestLoadQueue.size() > this.mDequeSize) {
                obj = this.mRequestLoadQueue.pollLast();
            }
            this.mRequestLoadQueue.notify();
        }
        return obj;
    }

    public void run() {
        try {
            guardedRun();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setQueueSize(int size) {
        synchronized (this.mRequestLoadQueue) {
            this.mDequeSize = size;
        }
    }
}