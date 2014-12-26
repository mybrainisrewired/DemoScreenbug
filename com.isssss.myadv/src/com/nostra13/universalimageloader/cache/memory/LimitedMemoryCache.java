package com.nostra13.universalimageloader.cache.memory;

import com.nostra13.universalimageloader.utils.L;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class LimitedMemoryCache<K, V> extends BaseMemoryCache<K, V> {
    private static final int MAX_NORMAL_CACHE_SIZE = 16777216;
    private static final int MAX_NORMAL_CACHE_SIZE_IN_MB = 16;
    private final AtomicInteger cacheSize;
    private final List<V> hardCache;
    private final int sizeLimit;

    public LimitedMemoryCache(int sizeLimit) {
        this.hardCache = Collections.synchronizedList(new LinkedList());
        this.sizeLimit = sizeLimit;
        this.cacheSize = new AtomicInteger();
        if (sizeLimit > 16777216) {
            L.w("You set too large memory cache size (more than %1$d Mb)", new Object[]{Integer.valueOf(MAX_NORMAL_CACHE_SIZE_IN_MB)});
        }
    }

    public void clear() {
        this.hardCache.clear();
        this.cacheSize.set(0);
        super.clear();
    }

    protected abstract int getSize(V v);

    protected int getSizeLimit() {
        return this.sizeLimit;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean put(K r8_key, V r9_value) {
        throw new UnsupportedOperationException("Method not decompiled: com.nostra13.universalimageloader.cache.memory.LimitedMemoryCache.put(java.lang.Object, java.lang.Object):boolean");
        /*
        r7 = this;
        r1 = 0;
        r4 = r7.getSize(r9);
        r3 = r7.getSizeLimit();
        r5 = r7.cacheSize;
        r0 = r5.get();
        if (r4 >= r3) goto L_0x0020;
    L_0x0011:
        r5 = r0 + r4;
        if (r5 > r3) goto L_0x0024;
    L_0x0015:
        r5 = r7.hardCache;
        r5.add(r9);
        r5 = r7.cacheSize;
        r5.addAndGet(r4);
        r1 = 1;
    L_0x0020:
        super.put(r8, r9);
        return r1;
    L_0x0024:
        r2 = r7.removeNext();
        r5 = r7.hardCache;
        r5 = r5.remove(r2);
        if (r5 == 0) goto L_0x0011;
    L_0x0030:
        r5 = r7.cacheSize;
        r6 = r7.getSize(r2);
        r6 = -r6;
        r0 = r5.addAndGet(r6);
        goto L_0x0011;
        */
    }

    public void remove(K key) {
        V value = super.get(key);
        if (value != null && this.hardCache.remove(value)) {
            this.cacheSize.addAndGet(-getSize(value));
        }
        super.remove(key);
    }

    protected abstract V removeNext();
}