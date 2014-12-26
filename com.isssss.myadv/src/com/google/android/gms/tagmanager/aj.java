package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.d.a;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

abstract class aj {
    private final Set<String> XU;
    private final String XV;

    public aj(String str, String... strArr) {
        this.XV = str;
        this.XU = new HashSet(strArr.length);
        int length = strArr.length;
        int i = 0;
        while (i < length) {
            this.XU.add(strArr[i]);
            i++;
        }
    }

    boolean a(Set<String> set) {
        return set.containsAll(this.XU);
    }

    public abstract boolean jX();

    public String kB() {
        return this.XV;
    }

    public Set<String> kC() {
        return this.XU;
    }

    public abstract a x(Map<String, a> map);
}