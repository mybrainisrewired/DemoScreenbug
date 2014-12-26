package com.nostra13.universalimageloader.cache.memory.impl;

import android.graphics.Bitmap;
import com.nostra13.universalimageloader.cache.memory.LimitedMemoryCache;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class LargestLimitedMemoryCache extends LimitedMemoryCache<String, Bitmap> {
    private final Map<Bitmap, Integer> valueSizes;

    public LargestLimitedMemoryCache(int sizeLimit) {
        super(sizeLimit);
        this.valueSizes = Collections.synchronizedMap(new HashMap());
    }

    public void clear() {
        this.valueSizes.clear();
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
        this.valueSizes.put(value, Integer.valueOf(getSize(value)));
        return true;
    }

    public void remove(String key) {
        Bitmap value = (Bitmap) super.get(key);
        if (value != null) {
            this.valueSizes.remove(value);
        }
        super.remove(key);
    }

    protected Bitmap removeNext() {
        Integer maxSize = null;
        Bitmap largestValue = null;
        Set<Entry<Bitmap, Integer>> entries = this.valueSizes.entrySet();
        synchronized (this.valueSizes) {
            Iterator it = entries.iterator();
            while (it.hasNext()) {
                Entry<Bitmap, Integer> entry = (Entry) it.next();
                if (largestValue == null) {
                    largestValue = (Bitmap) entry.getKey();
                    maxSize = (Integer) entry.getValue();
                } else {
                    Integer size = (Integer) entry.getValue();
                    if (size.intValue() > maxSize.intValue()) {
                        maxSize = size;
                        largestValue = (Bitmap) entry.getKey();
                    }
                }
            }
        }
        this.valueSizes.remove(largestValue);
        return largestValue;
    }
}