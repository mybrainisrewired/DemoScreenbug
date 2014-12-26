package com.google.android.gms.tagmanager;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import com.google.android.gms.internal.a;
import com.google.android.gms.internal.d;
import java.util.Map;

class h extends aj {
    private static final String ID;
    private final Context mContext;

    static {
        ID = a.y.toString();
    }

    public h(Context context) {
        super(ID, new String[0]);
        this.mContext = context;
    }

    public boolean jX() {
        return true;
    }

    public d.a x(Map<String, d.a> map) {
        try {
            return dh.r(Integer.valueOf(this.mContext.getPackageManager().getPackageInfo(this.mContext.getPackageName(), 0).versionCode));
        } catch (NameNotFoundException e) {
            bh.w("Package name " + this.mContext.getPackageName() + " not found. " + e.getMessage());
            return dh.lT();
        }
    }
}