package com.google.android.gms.internal;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class df {
    private int mOrientation;
    private String pN;
    private String pO;
    private String pP;
    private List<String> pQ;
    private String pR;
    private String pS;
    private List<String> pT;
    private long pU;
    private boolean pV;
    private final long pW;
    private List<String> pX;
    private long pY;

    public df() {
        this.pU = -1;
        this.pV = false;
        this.pW = -1;
        this.pY = -1;
        this.mOrientation = -1;
    }

    private static String a(Map<String, List<String>> map, String str) {
        List list = (List) map.get(str);
        return (list == null || list.isEmpty()) ? null : (String) list.get(0);
    }

    private static long b(Map<String, List<String>> map, String str) {
        List list = (List) map.get(str);
        if (!(list == null || list.isEmpty())) {
            String str2 = (String) list.get(0);
            try {
                return (long) (Float.parseFloat(str2) * 1000.0f);
            } catch (NumberFormatException e) {
                dw.z("Could not parse float from " + str + " header: " + str2);
            }
        }
        return -1;
    }

    private static List<String> c(Map<String, List<String>> map, String str) {
        List list = (List) map.get(str);
        if (!(list == null || list.isEmpty())) {
            String str2 = (String) list.get(0);
            if (str2 != null) {
                return Arrays.asList(str2.trim().split("\\s+"));
            }
        }
        return null;
    }

    private void f(Map<String, List<String>> map) {
        this.pN = a(map, "X-Afma-Ad-Size");
    }

    private void g(Map<String, List<String>> map) {
        List c = c(map, "X-Afma-Click-Tracking-Urls");
        if (c != null) {
            this.pQ = c;
        }
    }

    private void h(Map<String, List<String>> map) {
        List list = (List) map.get("X-Afma-Debug-Dialog");
        if (list != null && !list.isEmpty()) {
            this.pR = (String) list.get(0);
        }
    }

    private void i(Map<String, List<String>> map) {
        List c = c(map, "X-Afma-Tracking-Urls");
        if (c != null) {
            this.pT = c;
        }
    }

    private void j(Map<String, List<String>> map) {
        long b = b(map, "X-Afma-Interstitial-Timeout");
        if (b != -1) {
            this.pU = b;
        }
    }

    private void k(Map<String, List<String>> map) {
        this.pS = a(map, "X-Afma-ActiveView");
    }

    private void l(Map<String, List<String>> map) {
        List list = (List) map.get("X-Afma-Mediation");
        if (list != null && !list.isEmpty()) {
            this.pV = Boolean.valueOf((String) list.get(0)).booleanValue();
        }
    }

    private void m(Map<String, List<String>> map) {
        List c = c(map, "X-Afma-Manual-Tracking-Urls");
        if (c != null) {
            this.pX = c;
        }
    }

    private void n(Map<String, List<String>> map) {
        long b = b(map, "X-Afma-Refresh-Rate");
        if (b != -1) {
            this.pY = b;
        }
    }

    private void o(Map<String, List<String>> map) {
        List list = (List) map.get("X-Afma-Orientation");
        if (list != null && !list.isEmpty()) {
            String str = (String) list.get(0);
            if ("portrait".equalsIgnoreCase(str)) {
                this.mOrientation = dq.bA();
            } else if ("landscape".equalsIgnoreCase(str)) {
                this.mOrientation = dq.bz();
            }
        }
    }

    public void a(String str, Map<String, List<String>> map, String str2) {
        this.pO = str;
        this.pP = str2;
        e(map);
    }

    public void e(Map map) {
        f(map);
        g(map);
        h(map);
        i(map);
        j(map);
        l(map);
        m(map);
        n(map);
        o(map);
        k(map);
    }

    public cz g(long j) {
        return new cz(this.pO, this.pP, this.pQ, this.pT, this.pU, this.pV, -1, this.pX, this.pY, this.mOrientation, this.pN, j, this.pR, this.pS);
    }
}