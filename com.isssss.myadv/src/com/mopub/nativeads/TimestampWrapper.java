package com.mopub.nativeads;

import android.os.SystemClock;

class TimestampWrapper<T> {
    long mCreatedTimestamp;
    final T mInstance;

    TimestampWrapper(T instance) {
        this.mInstance = instance;
        this.mCreatedTimestamp = SystemClock.uptimeMillis();
    }
}