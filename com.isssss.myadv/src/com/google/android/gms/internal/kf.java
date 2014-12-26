package com.google.android.gms.internal;

import android.net.Uri;
import android.util.Log;
import com.google.android.gms.wearable.c;
import com.google.android.gms.wearable.d;
import com.millennialmedia.android.MMAdView;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class kf implements c {
    private byte[] Nf;
    private Map<String, d> adD;
    private Set<String> adE;
    private Uri mUri;

    public kf(c cVar) {
        this.mUri = cVar.getUri();
        this.Nf = cVar.getData();
        Map hashMap = new HashMap();
        Iterator it = cVar.ma().entrySet().iterator();
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            if (entry.getKey() != null) {
                hashMap.put(entry.getKey(), ((d) entry.getValue()).freeze());
            }
        }
        this.adD = Collections.unmodifiableMap(hashMap);
        this.adE = Collections.unmodifiableSet(cVar.mb());
    }

    public /* synthetic */ Object freeze() {
        return mg();
    }

    public byte[] getData() {
        return this.Nf;
    }

    public Uri getUri() {
        return this.mUri;
    }

    public boolean isDataValid() {
        return true;
    }

    public Map<String, d> ma() {
        return this.adD;
    }

    @Deprecated
    public Set<String> mb() {
        return this.adE;
    }

    public c mg() {
        return this;
    }

    public String toString() {
        return toString(Log.isLoggable("DataItem", MMAdView.TRANSITION_DOWN));
    }

    public String toString(boolean verbose) {
        StringBuilder stringBuilder = new StringBuilder("DataItemEntity[");
        stringBuilder.append("@");
        stringBuilder.append(Integer.toHexString(hashCode()));
        stringBuilder.append(",dataSz=" + (this.Nf == null ? "null" : Integer.valueOf(this.Nf.length)));
        stringBuilder.append(", numAssets=" + this.adD.size());
        stringBuilder.append(", uri=" + this.mUri);
        if (verbose) {
            String str;
            stringBuilder.append("\n  tags=[");
            Iterator it = this.adE.iterator();
            int i = 0;
            while (it.hasNext()) {
                str = (String) it.next();
                if (i != 0) {
                    stringBuilder.append(", ");
                } else {
                    boolean z = 1;
                }
                stringBuilder.append(str);
            }
            stringBuilder.append("]\n  assets: ");
            Iterator it2 = this.adD.keySet().iterator();
            while (it2.hasNext()) {
                str = (String) it2.next();
                stringBuilder.append("\n    " + str + ": " + this.adD.get(str));
            }
            stringBuilder.append("\n  ]");
            return stringBuilder.toString();
        } else {
            stringBuilder.append("]");
            return stringBuilder.toString();
        }
    }
}