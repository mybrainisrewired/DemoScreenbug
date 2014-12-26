package com.google.android.gms.tagmanager;

import android.content.Context;
import com.google.android.gms.internal.a;
import com.google.android.gms.internal.b;
import com.google.android.gms.internal.d;
import java.util.Map;

class e extends aj {
    private static final String ID;
    private static final String WA;
    private static final String WB;
    private final Context kI;

    static {
        ID = a.W.toString();
        WA = b.bH.toString();
        WB = b.bK.toString();
    }

    public e(Context context) {
        super(ID, new String[]{WB});
        this.kI = context;
    }

    public boolean jX() {
        return true;
    }

    public d.a x(Map<String, d.a> map) {
        d.a aVar = (d.a) map.get(WB);
        if (aVar == null) {
            return dh.lT();
        }
        String j = dh.j(aVar);
        aVar = (d.a) map.get(WA);
        String e = ay.e(this.kI, j, aVar != null ? dh.j(aVar) : null);
        return e != null ? dh.r(e) : dh.lT();
    }
}