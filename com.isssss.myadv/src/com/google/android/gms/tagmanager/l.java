package com.google.android.gms.tagmanager;

import android.os.Build.VERSION;

class l<K, V> {
    final a<K, V> WH;

    public static interface a<K, V> {
        int sizeOf(K k, V v);
    }

    public l() {
        this.WH = new a<K, V>() {
            public int sizeOf(K key, V value) {
                return 1;
            }
        };
    }

    public k<K, V> a(int i, a<K, V> aVar) {
        if (i > 0) {
            return jZ() < 12 ? new cz(i, aVar) : new bb(i, aVar);
        } else {
            throw new IllegalArgumentException("maxSize <= 0");
        }
    }

    int jZ() {
        return VERSION.SDK_INT;
    }
}