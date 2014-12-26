package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.a;
import com.google.android.gms.internal.b;
import com.google.android.gms.internal.d;
import java.util.Map;

class m extends aj {
    private static final String ID;
    private static final String VALUE;

    static {
        ID = a.A.toString();
        VALUE = b.ew.toString();
    }

    public m() {
        super(ID, new String[]{VALUE});
    }

    public static String ka() {
        return ID;
    }

    public static String kb() {
        return VALUE;
    }

    public boolean jX() {
        return true;
    }

    public d.a x(Map<String, d.a> map) {
        return (d.a) map.get(VALUE);
    }
}