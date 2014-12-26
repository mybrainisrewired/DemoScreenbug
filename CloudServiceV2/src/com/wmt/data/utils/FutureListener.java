package com.wmt.data.utils;

public interface FutureListener<T> {
    void onFutureDone(Future<T> future);
}