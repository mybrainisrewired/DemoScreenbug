package com.google.android.gms.tagmanager;

import android.support.v4.media.TransportMediator;
import com.google.android.gms.internal.a;
import com.google.android.gms.internal.b;
import com.google.android.gms.internal.d;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

class cg extends aj {
    private static final String ID;
    private static final String YZ;
    private static final String Za;
    private static final String Zb;
    private static final String Zc;

    static {
        ID = a.ae.toString();
        YZ = b.bi.toString();
        Za = b.bj.toString();
        Zb = b.cF.toString();
        Zc = b.cz.toString();
    }

    public cg() {
        super(ID, new String[]{YZ, Za});
    }

    public boolean jX() {
        return true;
    }

    public d.a x(Map<String, d.a> map) {
        d.a aVar = (d.a) map.get(YZ);
        d.a aVar2 = (d.a) map.get(Za);
        if (aVar == null || aVar == dh.lT() || aVar2 == null || aVar2 == dh.lT()) {
            return dh.lT();
        }
        int intValue;
        int i = TransportMediator.FLAG_KEY_MEDIA_FAST_FORWARD;
        if (dh.n((d.a) map.get(Zb)).booleanValue()) {
            i = 66;
        }
        d.a aVar3 = (d.a) map.get(Zc);
        if (aVar3 != null) {
            Long l = dh.l(aVar3);
            if (l == dh.lO()) {
                return dh.lT();
            }
            intValue = l.intValue();
            if (intValue < 0) {
                return dh.lT();
            }
        } else {
            intValue = 1;
        }
        try {
            CharSequence j = dh.j(aVar);
            Object obj = null;
            Matcher matcher = Pattern.compile(dh.j(aVar2), i).matcher(j);
            if (matcher.find() && matcher.groupCount() >= intValue) {
                obj = matcher.group(intValue);
            }
            return obj == null ? dh.lT() : dh.r(obj);
        } catch (PatternSyntaxException e) {
            return dh.lT();
        }
    }
}