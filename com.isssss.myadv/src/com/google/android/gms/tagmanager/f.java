package com.google.android.gms.tagmanager;

import android.content.Context;
import com.google.android.gms.internal.a;
import com.google.android.gms.internal.d;
import java.util.Map;

class f extends aj {
    private static final String ID;
    private final Context mContext;

    static {
        ID = a.w.toString();
    }

    public f(Context context) {
        super(ID, new String[0]);
        this.mContext = context;
    }

    public boolean jX() {
        return true;
    }

    public d.a x(Map<String, d.a> map) {
        return dh.r(this.mContext.getPackageName());
    }
}