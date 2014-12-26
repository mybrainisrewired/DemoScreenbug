package com.google.android.gms.internal;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class fu<K, V> {
    private final LinkedHashMap<K, V> DL;
    private int DM;
    private int DN;
    private int DO;
    private int DP;
    private int DQ;
    private int DR;
    private int size;

    public fu(int i) {
        if (i <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        this.DM = i;
        this.DL = new LinkedHashMap(0, 0.75f, true);
    }

    private int c(K k, V v) {
        int sizeOf = sizeOf(k, v);
        if (sizeOf >= 0) {
            return sizeOf;
        }
        throw new IllegalStateException("Negative size: " + k + "=" + v);
    }

    protected V create(K key) {
        return null;
    }

    protected void entryRemoved(boolean evicted, K key, V oldValue, V newValue) {
    }

    public final void evictAll() {
        trimToSize(-1);
    }

    public final V get(K key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }
        synchronized (this) {
            V v = this.DL.get(key);
            if (v != null) {
                this.DQ++;
                return v;
            } else {
                this.DR++;
                V create = create(key);
                if (create == null) {
                    return null;
                }
                synchronized (this) {
                    this.DO++;
                    v = this.DL.put(key, create);
                    if (v != null) {
                        this.DL.put(key, v);
                    } else {
                        this.size += c(key, create);
                    }
                }
                if (v != null) {
                    entryRemoved(false, key, create, v);
                    return v;
                } else {
                    trimToSize(this.DM);
                    return create;
                }
            }
        }
    }

    public final V put(K key, V value) {
        if (key == null || value == null) {
            throw new NullPointerException("key == null || value == null");
        }
        V put;
        synchronized (this) {
            this.DN++;
            this.size += c(key, value);
            put = this.DL.put(key, value);
            if (put != null) {
                this.size -= c(key, put);
            }
        }
        if (put != null) {
            entryRemoved(false, key, put, value);
        }
        trimToSize(this.DM);
        return put;
    }

    public final synchronized int size() {
        return this.size;
    }

    protected int sizeOf(K key, V value) {
        return 1;
    }

    public final synchronized String toString() {
        String format;
        int i = 0;
        synchronized (this) {
            int i2 = this.DQ + this.DR;
            if (i2 != 0) {
                i = (this.DQ * 100) / i2;
            }
            format = String.format("LruCache[maxSize=%d,hits=%d,misses=%d,hitRate=%d%%]", new Object[]{Integer.valueOf(this.DM), Integer.valueOf(this.DQ), Integer.valueOf(this.DR), Integer.valueOf(i)});
        }
        return format;
    }

    public void trimToSize(int maxSize) {
        while (true) {
            synchronized (this) {
                if (this.size >= 0) {
                    if (!this.DL.isEmpty() || this.size == 0) {
                        if (this.size > maxSize && !this.DL.isEmpty()) {
                            Entry entry = (Entry) this.DL.entrySet().iterator().next();
                            Object key = entry.getKey();
                            Object value = entry.getValue();
                            this.DL.remove(key);
                            this.size -= c(key, value);
                            this.DP++;
                            entryRemoved(true, key, value, null);
                        }
                        return;
                    }
                }
                throw new IllegalStateException(getClass().getName() + ".sizeOf() is reporting inconsistent results!");
            }
        }
    }
}