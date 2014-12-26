package com.mopub.nativeads;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

abstract class TaskManager<T> {
    protected final AtomicInteger mCompletedCount;
    protected final AtomicBoolean mFailed;
    protected final TaskManagerListener<T> mImageTaskManagerListener;
    protected final Map<String, T> mResults;
    protected final int mSize;

    static interface TaskManagerListener<T> {
        void onFail();

        void onSuccess(Map<String, T> map);
    }

    TaskManager(List<String> urls, TaskManagerListener<T> imageTaskManagerListener) throws IllegalArgumentException {
        if (urls == null) {
            throw new IllegalArgumentException("Urls list cannot be null");
        } else if (urls.contains(null)) {
            throw new IllegalArgumentException("Urls list cannot contain null");
        } else if (imageTaskManagerListener == null) {
            throw new IllegalArgumentException("ImageTaskManagerListener cannot be null");
        } else {
            this.mSize = urls.size();
            this.mImageTaskManagerListener = imageTaskManagerListener;
            this.mCompletedCount = new AtomicInteger(0);
            this.mFailed = new AtomicBoolean(false);
            this.mResults = Collections.synchronizedMap(new HashMap(this.mSize));
        }
    }

    abstract void execute();
}