package com.google.android.gms.internal;

import android.net.Uri;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.common.data.b;
import com.google.android.gms.wearable.c;
import com.google.android.gms.wearable.d;
import com.inmobi.androidsdk.IMBrowserActivity;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class kg extends b implements c {
    private final int LE;

    public kg(DataHolder dataHolder, int i, int i2) {
        super(dataHolder, i);
        this.LE = i2;
    }

    public /* synthetic */ Object freeze() {
        return mg();
    }

    public byte[] getData() {
        return getByteArray(IMBrowserActivity.EXPANDDATA);
    }

    public Uri getUri() {
        return Uri.parse(getString("path"));
    }

    public Map<String, d> ma() {
        Map<String, d> hashMap = new HashMap(this.LE);
        int i = 0;
        while (i < this.LE) {
            ke keVar = new ke(this.BB, this.BD + i);
            if (keVar.mc() != null) {
                hashMap.put(keVar.mc(), keVar);
            }
            i++;
        }
        return hashMap;
    }

    @Deprecated
    public Set<String> mb() {
        Set hashSet = new HashSet();
        String string = getString("tags");
        if (string != null) {
            String[] split = string.split("\\|");
            int length = split.length;
            int i = 0;
            while (i < length) {
                hashSet.add(split[i]);
                i++;
            }
        }
        return hashSet;
    }

    public c mg() {
        return new kf(this);
    }
}