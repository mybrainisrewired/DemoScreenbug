package com.google.android.gms.tagmanager;

import android.util.LruCache;
import com.google.android.gms.tagmanager.l.a;

class bb<K, V> implements k<K, V> {
    private LruCache<K, V> Yu;

    class AnonymousClass_1 extends LruCache<K, V> {
        final /* synthetic */ a Yv;

        AnonymousClass_1(int i, a aVar) {
            this.Yv = aVar;
            super(i);
        }

        protected int sizeOf(K key, V value) {
            return this.Yv.sizeOf(key, value);
        }
    }

    bb(int i, a<K, V> aVar) {
        this.Yu = new AnonymousClass_1(i, aVar);
    }

    public void e(K k, V v) {
        this.Yu.put(k, v);
    }

    public V get(K key) {
        return this.Yu.get(key);
    }
}