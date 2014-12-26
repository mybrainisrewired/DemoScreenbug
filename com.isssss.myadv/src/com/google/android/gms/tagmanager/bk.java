package com.google.android.gms.tagmanager;

import android.content.Context;
import android.provider.Settings.Secure;
import com.google.android.gms.internal.a;
import com.google.android.gms.internal.d;
import java.util.Map;

class bk extends aj {
    private static final String ID;
    private final Context mContext;

    static {
        ID = a.ad.toString();
    }

    public bk(Context context) {
        super(ID, new String[0]);
        this.mContext = context;
    }

    protected String G(Context context) {
        return Secure.getString(context.getContentResolver(), "android_id");
    }

    public boolean jX() {
        return true;
    }

    public d.a x(Map<String, d.a> map) {
        String G = G(this.mContext);
        return G == null ? dh.lT() : dh.r(G);
    }
}