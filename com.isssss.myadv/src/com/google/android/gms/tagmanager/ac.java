package com.google.android.gms.tagmanager;

import android.util.Base64;
import com.google.android.gms.internal.a;
import com.google.android.gms.internal.b;
import com.google.android.gms.internal.d;
import com.millennialmedia.android.MMAdView;
import java.util.Map;

class ac extends aj {
    private static final String ID;
    private static final String XQ;
    private static final String XR;
    private static final String XS;
    private static final String XT;

    static {
        ID = a.Y.toString();
        XQ = b.bi.toString();
        XR = b.di.toString();
        XS = b.cH.toString();
        XT = b.dq.toString();
    }

    public ac() {
        super(ID, new String[]{XQ});
    }

    public boolean jX() {
        return true;
    }

    public d.a x(Map<String, d.a> map) {
        d.a aVar = (d.a) map.get(XQ);
        if (aVar == null || aVar == dh.lT()) {
            return dh.lT();
        }
        String j = dh.j(aVar);
        aVar = (d.a) map.get(XS);
        String j2 = aVar == null ? "text" : dh.j(aVar);
        aVar = (d.a) map.get(XT);
        String j3 = aVar == null ? "base16" : dh.j(aVar);
        aVar = (d.a) map.get(XR);
        int i = (aVar == null || !dh.n(aVar).booleanValue()) ? 2 : MMAdView.TRANSITION_DOWN;
        try {
            byte[] bytes;
            Object d;
            if ("text".equals(j2)) {
                bytes = j.getBytes();
            } else if ("base16".equals(j2)) {
                bytes = j.bm(j);
            } else if ("base64".equals(j2)) {
                bytes = Base64.decode(j, i);
            } else if ("base64url".equals(j2)) {
                bytes = Base64.decode(j, i | 8);
            } else {
                bh.w("Encode: unknown input format: " + j2);
                return dh.lT();
            }
            if ("base16".equals(j3)) {
                d = j.d(bytes);
            } else if ("base64".equals(j3)) {
                d = Base64.encodeToString(bytes, i);
            } else if ("base64url".equals(j3)) {
                d = Base64.encodeToString(bytes, i | 8);
            } else {
                bh.w("Encode: unknown output format: " + j3);
                return dh.lT();
            }
            return dh.r(d);
        } catch (IllegalArgumentException e) {
            bh.w("Encode: invalid input:");
            return dh.lT();
        }
    }
}