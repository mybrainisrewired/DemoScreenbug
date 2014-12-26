package com.google.android.gms.analytics;

import com.mopub.common.Preconditions;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class ab {
    private final Map<String, Integer> vt;
    private final Map<String, String> vu;
    private final boolean vv;
    private final String vw;

    ab(String str, boolean z) {
        this.vt = new HashMap();
        this.vu = new HashMap();
        this.vv = z;
        this.vw = str;
    }

    void c(String str, int i) {
        if (this.vv) {
            Integer num = (Integer) this.vt.get(str);
            if (num == null) {
                num = Integer.valueOf(0);
            }
            this.vt.put(str, Integer.valueOf(num.intValue() + i));
        }
    }

    String cU() {
        if (!this.vv) {
            return Preconditions.EMPTY_ARGUMENTS;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.vw);
        Iterator it = this.vt.keySet().iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            stringBuilder.append("&").append(str).append("=").append(this.vt.get(str));
        }
        it = this.vu.keySet().iterator();
        while (it.hasNext()) {
            str = (String) it.next();
            stringBuilder.append("&").append(str).append("=").append((String) this.vu.get(str));
        }
        return stringBuilder.toString();
    }
}