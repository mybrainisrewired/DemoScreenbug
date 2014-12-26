package com.nostra13.universalimageloader.cache.memory.impl;

import com.nostra13.universalimageloader.cache.memory.MemoryCacheAware;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

public class FuzzyKeyMemoryCache<K, V> implements MemoryCacheAware<K, V> {
    private final MemoryCacheAware<K, V> cache;
    private final Comparator<K> keyComparator;

    public FuzzyKeyMemoryCache(MemoryCacheAware<K, V> cache, Comparator<K> keyComparator) {
        this.cache = cache;
        this.keyComparator = keyComparator;
    }

    public void clear() {
        this.cache.clear();
    }

    public V get(K key) {
        return this.cache.get(key);
    }

    public Collection<K> keys() {
        return this.cache.keys();
    }

    public boolean put(K key, V value) {
        synchronized (this.cache) {
            K keyToRemove = null;
            Iterator it = this.cache.keys().iterator();
            while (it.hasNext()) {
                K cacheKey = it.next();
                if (this.keyComparator.compare(key, cacheKey) == 0) {
                    keyToRemove = cacheKey;
                    break;
                }
            }
            if (keyToRemove != null) {
                this.cache.remove(keyToRemove);
            }
        }
        return this.cache.put(key, value);
    }

    public void remove(K key) {
        this.cache.remove(key);
    }
}