package com.google.android.gms.tagmanager;

import android.content.Context;
import android.net.Uri;
import android.net.Uri.Builder;
import com.google.android.gms.internal.b;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

class i extends df {
    private static final String ID;
    private static final String URL;
    private static final String WC;
    private static final String WD;
    static final String WE;
    private static final Set<String> WF;
    private final a WG;
    private final Context mContext;

    public static interface a {
        aq jY();
    }

    class AnonymousClass_1 implements com.google.android.gms.tagmanager.i.a {
        final /* synthetic */ Context pB;

        AnonymousClass_1(Context context) {
            this.pB = context;
        }

        public aq jY() {
            return y.F(this.pB);
        }
    }

    static {
        ID = com.google.android.gms.internal.a.ap.toString();
        URL = b.eo.toString();
        WC = b.aX.toString();
        WD = b.en.toString();
        WE = "gtm_" + ID + "_unrepeatable";
        WF = new HashSet();
    }

    public i(Context context) {
        this(context, new AnonymousClass_1(context));
    }

    i(Context context, a aVar) {
        super(ID, new String[]{URL});
        this.WG = aVar;
        this.mContext = context;
    }

    private synchronized boolean bj(String str) {
        boolean z = true;
        synchronized (this) {
            if (!bl(str)) {
                if (bk(str)) {
                    WF.add(str);
                } else {
                    z = false;
                }
            }
        }
        return z;
    }

    boolean bk(String str) {
        return this.mContext.getSharedPreferences(WE, 0).contains(str);
    }

    boolean bl(String str) {
        return WF.contains(str);
    }

    public void z(Map<String, com.google.android.gms.internal.d.a> map) {
        String j = map.get(WD) != null ? dh.j((com.google.android.gms.internal.d.a) map.get(WD)) : null;
        if (j == null || !bj(j)) {
            Builder buildUpon = Uri.parse(dh.j((com.google.android.gms.internal.d.a) map.get(URL))).buildUpon();
            com.google.android.gms.internal.d.a aVar = (com.google.android.gms.internal.d.a) map.get(WC);
            if (aVar != null) {
                Object o = dh.o(aVar);
                if (o instanceof List) {
                    Iterator it = ((List) o).iterator();
                    while (it.hasNext()) {
                        o = it.next();
                        if (o instanceof Map) {
                            Iterator it2 = ((Map) o).entrySet().iterator();
                            while (it2.hasNext()) {
                                Entry entry = (Entry) it2.next();
                                buildUpon.appendQueryParameter(entry.getKey().toString(), entry.getValue().toString());
                            }
                        } else {
                            bh.w("ArbitraryPixel: additional params contains non-map: not sending partial hit: " + buildUpon.build().toString());
                            return;
                        }
                    }
                } else {
                    bh.w("ArbitraryPixel: additional params not a list: not sending partial hit: " + buildUpon.build().toString());
                    return;
                }
            }
            String toString = buildUpon.build().toString();
            this.WG.jY().bz(toString);
            bh.y("ArbitraryPixel: url = " + toString);
            if (j != null) {
                synchronized (i.class) {
                    WF.add(j);
                    cy.a(this.mContext, WE, j, "true");
                }
            }
        }
    }
}