package com.google.android.gms.analytics;

import com.mopub.common.Preconditions;

public final class o {
    private static String b(String str, int i) {
        if (i >= 1) {
            return str + i;
        }
        aa.w("index out of range for " + str + " (" + i + ")");
        return Preconditions.EMPTY_ARGUMENTS;
    }

    static String q(int i) {
        return b("&cd", i);
    }

    static String r(int i) {
        return b("&cm", i);
    }
}