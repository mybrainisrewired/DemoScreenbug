package com.wmt.util;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

public final class LruCache<K, V> {
    private final HashMap<K, V> mLruMap;
    private ReferenceQueue<V> mQueue;
    private final HashMap<K, Entry<K, V>> mWeakMap;

    class AnonymousClass_1 extends LinkedHashMap<K, V> {
        final /* synthetic */ int val$capacity;

        AnonymousClass_1(int x0, float x1, boolean x2, int i) {
            this.val$capacity = i;
            super(x0, x1, x2);
        }

        protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldest) {
            return size() > this.val$capacity;
        }
    }

    private static class Entry<K, V> extends WeakReference<V> {
        K mKey;

        public Entry(K key, V value, ReferenceQueue queue) {
            super(value, queue);
            this.mKey = key;
        }
    }

    public LruCache(int capacity) {
        this.mWeakMap = new HashMap();
        this.mQueue = new ReferenceQueue();
        this.mLruMap = new AnonymousClass_1(capacity, 0.75f, true, capacity);
    }

    private void cleanUpWeakMap() {
        Entry<K, V> entry = (Entry) this.mQueue.poll();
        while (entry != null) {
            this.mWeakMap.remove(entry.mKey);
            entry = this.mQueue.poll();
        }
    }

    public synchronized void clear() {
        this.mLruMap.clear();
        this.mWeakMap.clear();
        this.mQueue = new ReferenceQueue();
    }

    public synchronized V get(K key) {
        V value;
        cleanUpWeakMap();
        value = this.mLruMap.get(key);
        if (value == null) {
            Entry<K, V> entry = (Entry) this.mWeakMap.get(key);
            value = entry == null ? null : entry.get();
        }
        return value;
    }

    public int getSize() {
        return this.mLruMap.size();
    }

    public synchronized Set<K> keySet() {
        return this.mLruMap.keySet();
    }

    public synchronized V put(K key, V value) {
        Entry<K, V> entry;
        cleanUpWeakMap();
        this.mLruMap.put(key, value);
        entry = (Entry) this.mWeakMap.put(key, new Entry(key, value, this.mQueue));
        return entry == null ? null : entry.get();
    }

    public synchronized void remove(K key) {
        cleanUpWeakMap();
        this.mLruMap.remove(key);
        this.mWeakMap.remove(key);
    }

    public synchronized Collection<V> values() {
        return this.mLruMap.values();
    }
}