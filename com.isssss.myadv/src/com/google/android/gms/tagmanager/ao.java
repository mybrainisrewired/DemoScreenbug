package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.a;
import com.google.android.gms.internal.b;
import com.google.android.gms.internal.d;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

class ao extends aj {
    private static final String ID;
    private static final String XQ;
    private static final String XS;
    private static final String XW;

    static {
        ID = a.aa.toString();
        XQ = b.bi.toString();
        XW = b.aZ.toString();
        XS = b.cH.toString();
    }

    public ao() {
        super(ID, new String[]{XQ});
    }

    private byte[] c(String str, byte[] bArr) throws NoSuchAlgorithmException {
        MessageDigest instance = MessageDigest.getInstance(str);
        instance.update(bArr);
        return instance.digest();
    }

    public boolean jX() {
        return true;
    }

    public d.a x(Map<String, d.a> map) {
        d.a aVar = (d.a) map.get(XQ);
        if (aVar == null || aVar == dh.lT()) {
            return dh.lT();
        }
        byte[] bytes;
        String j = dh.j(aVar);
        aVar = (d.a) map.get(XW);
        String j2 = aVar == null ? "MD5" : dh.j(aVar);
        aVar = (d.a) map.get(XS);
        String j3 = aVar == null ? "text" : dh.j(aVar);
        if ("text".equals(j3)) {
            bytes = j.getBytes();
        } else if ("base16".equals(j3)) {
            bytes = j.bm(j);
        } else {
            bh.w("Hash: unknown input format: " + j3);
            return dh.lT();
        }
        try {
            return dh.r(j.d(c(j2, bytes)));
        } catch (NoSuchAlgorithmException e) {
            bh.w("Hash: unknown algorithm: " + j2);
            return dh.lT();
        }
    }
}