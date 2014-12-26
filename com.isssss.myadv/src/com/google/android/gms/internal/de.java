package com.google.android.gms.internal;

import com.google.android.gms.plus.PlusShare;
import com.inmobi.commons.analytics.db.AnalyticsSQLiteHelper;
import java.util.Map;

public final class de {
    private dz lC;
    private final Object li;
    private int oS;
    private String pI;
    private String pJ;
    public final bb pK;
    public final bb pL;

    public de(String str) {
        this.li = new Object();
        this.oS = -2;
        this.pK = new bb() {
            public void b(dz dzVar, Map<String, String> map) {
                synchronized (de.this.li) {
                    String str = (String) map.get("errors");
                    dw.z("Invalid " + ((String) map.get(AnalyticsSQLiteHelper.EVENT_LIST_TYPE)) + " request error: " + str);
                    de.this.oS = 1;
                    de.this.li.notify();
                }
            }
        };
        this.pL = new bb() {
            public void b(dz dzVar, Map<String, String> map) {
                synchronized (de.this.li) {
                    String str = (String) map.get(PlusShare.KEY_CALL_TO_ACTION_URL);
                    if (str == null) {
                        dw.z("URL missing in loadAdUrl GMSG.");
                    } else {
                        if (str.contains("%40mediation_adapters%40")) {
                            str = str.replaceAll("%40mediation_adapters%40", dn.b(dzVar.getContext(), (String) map.get("check_adapters"), de.this.pI));
                            dw.y("Ad request URL modified to " + str);
                        }
                        de.this.pJ = str;
                        de.this.li.notify();
                    }
                }
            }
        };
        this.pI = str;
    }

    public void b(dz dzVar) {
        synchronized (this.li) {
            this.lC = dzVar;
        }
    }

    public String bj() {
        String str;
        synchronized (this.li) {
            while (this.pJ == null && this.oS == -2) {
                try {
                    this.li.wait();
                } catch (InterruptedException e) {
                    dw.z("Ad request service was interrupted.");
                    str = null;
                }
            }
            str = this.pJ;
        }
        return str;
    }

    public int getErrorCode() {
        int i;
        synchronized (this.li) {
            i = this.oS;
        }
        return i;
    }
}