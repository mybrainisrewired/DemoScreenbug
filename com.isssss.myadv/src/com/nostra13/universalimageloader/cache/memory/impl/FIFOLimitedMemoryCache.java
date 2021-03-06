package com.nostra13.universalimageloader.cache.memory.impl;

import android.graphics.Bitmap;
import com.nostra13.universalimageloader.cache.memory.LimitedMemoryCache;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class FIFOLimitedMemoryCache extends LimitedMemoryCache<String, Bitmap> {
    private final List<Bitmap> queue;

    public FIFOLimitedMemoryCache(int sizeLimit) {
        super(sizeLimit);
        this.queue = Collections.synchronizedList(new LinkedList());
    }

    public void clear() {
        this.queue.clear();
        super.clear();
    }

    protected Reference<Bitmap> createReference(Bitmap value) {
        return new WeakReference(value);
    }

    protected int getSize(Bitmap value) {
        return value.getRowBytes() * value.getHeight();
    }

    public boolean put(String key, Bitmap value) {
        if (!super.put(key, value)) {
            return false;
        }
        this.queue.add(value);
        return true;
    }

    public void remove(String key) {
        Bitmap value = (Bitmap) super.get(key);
        if (value != null) {
            this.queue.remove(value);
        }
        super.remove(key);
    }

    protected Bitmap removeNext() {
        return (Bitmap) this.queue.remove(0);
    }
}