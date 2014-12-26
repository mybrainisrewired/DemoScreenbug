package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.d.a;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

class dk {
    private static by<a> a(by<a> byVar) {
        try {
            return new by(dh.r(cd(dh.j((a) byVar.getObject()))), byVar.kQ());
        } catch (UnsupportedEncodingException e) {
            bh.b("Escape URI: unsupported encoding", e);
            return byVar;
        }
    }

    private static by<a> a(by<a> byVar, int i) {
        if (q((a) byVar.getObject())) {
            switch (i) {
                case ApiEventType.API_MRAID_RESIZE:
                    return a(byVar);
                default:
                    bh.w("Unsupported Value Escaping: " + i);
                    return byVar;
            }
        } else {
            bh.w("Escaping can only be applied to strings.");
            return byVar;
        }
    }

    static by<a> a(by<a> byVar, int... iArr) {
        int length = iArr.length;
        int i = 0;
        while (i < length) {
            byVar = a(byVar, iArr[i]);
            i++;
        }
        return byVar;
    }

    static String cd(String str) throws UnsupportedEncodingException {
        return URLEncoder.encode(str, "UTF-8").replaceAll("\\+", "%20");
    }

    private static boolean q(a aVar) {
        return dh.o(aVar) instanceof String;
    }
}