package com.google.android.gms.analytics;

import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import java.util.SortedSet;
import java.util.TreeSet;

class u {
    private static final u tH;
    private SortedSet<a> tE;
    private StringBuilder tF;
    private boolean tG;

    public enum a {
        MAP_BUILDER_SET,
        MAP_BUILDER_SET_ALL,
        MAP_BUILDER_GET,
        MAP_BUILDER_SET_CAMPAIGN_PARAMS,
        BLANK_04,
        BLANK_05,
        BLANK_06,
        BLANK_07,
        BLANK_08,
        GET,
        SET,
        SEND,
        BLANK_12,
        BLANK_13,
        BLANK_14,
        BLANK_15,
        BLANK_16,
        BLANK_17,
        BLANK_18,
        BLANK_19,
        BLANK_20,
        BLANK_21,
        BLANK_22,
        BLANK_23,
        BLANK_24,
        BLANK_25,
        BLANK_26,
        BLANK_27,
        BLANK_28,
        BLANK_29,
        SET_EXCEPTION_PARSER,
        GET_EXCEPTION_PARSER,
        CONSTRUCT_TRANSACTION,
        CONSTRUCT_EXCEPTION,
        CONSTRUCT_RAW_EXCEPTION,
        CONSTRUCT_TIMING,
        CONSTRUCT_SOCIAL,
        BLANK_37,
        BLANK_38,
        GET_TRACKER,
        GET_DEFAULT_TRACKER,
        SET_DEFAULT_TRACKER,
        SET_APP_OPT_OUT,
        GET_APP_OPT_OUT,
        DISPATCH,
        SET_DISPATCH_PERIOD,
        BLANK_46,
        REPORT_UNCAUGHT_EXCEPTIONS,
        SET_AUTO_ACTIVITY_TRACKING,
        SET_SESSION_TIMEOUT,
        CONSTRUCT_EVENT,
        CONSTRUCT_ITEM,
        BLANK_52,
        BLANK_53,
        SET_DRY_RUN,
        GET_DRY_RUN,
        SET_LOGGER,
        SET_FORCE_LOCAL_DISPATCH,
        GET_TRACKER_NAME,
        CLOSE_TRACKER,
        EASY_TRACKER_ACTIVITY_START,
        EASY_TRACKER_ACTIVITY_STOP,
        CONSTRUCT_APP_VIEW;

        static {
            tI = new com.google.android.gms.analytics.u.a("MAP_BUILDER_SET", 0);
            tJ = new com.google.android.gms.analytics.u.a("MAP_BUILDER_SET_ALL", 1);
            tK = new com.google.android.gms.analytics.u.a("MAP_BUILDER_GET", 2);
            tL = new com.google.android.gms.analytics.u.a("MAP_BUILDER_SET_CAMPAIGN_PARAMS", 3);
            tM = new com.google.android.gms.analytics.u.a("BLANK_04", 4);
            tN = new com.google.android.gms.analytics.u.a("BLANK_05", 5);
            tO = new com.google.android.gms.analytics.u.a("BLANK_06", 6);
            tP = new com.google.android.gms.analytics.u.a("BLANK_07", 7);
            tQ = new com.google.android.gms.analytics.u.a("BLANK_08", 8);
            tR = new com.google.android.gms.analytics.u.a("GET", 9);
            tS = new com.google.android.gms.analytics.u.a("SET", 10);
            tT = new com.google.android.gms.analytics.u.a("SEND", 11);
            tU = new com.google.android.gms.analytics.u.a("BLANK_12", 12);
            tV = new com.google.android.gms.analytics.u.a("BLANK_13", 13);
            tW = new com.google.android.gms.analytics.u.a("BLANK_14", 14);
            tX = new com.google.android.gms.analytics.u.a("BLANK_15", 15);
            tY = new com.google.android.gms.analytics.u.a("BLANK_16", 16);
            tZ = new com.google.android.gms.analytics.u.a("BLANK_17", 17);
            ua = new com.google.android.gms.analytics.u.a("BLANK_18", 18);
            ub = new com.google.android.gms.analytics.u.a("BLANK_19", 19);
            uc = new com.google.android.gms.analytics.u.a("BLANK_20", 20);
            ud = new com.google.android.gms.analytics.u.a("BLANK_21", 21);
            ue = new com.google.android.gms.analytics.u.a("BLANK_22", 22);
            uf = new com.google.android.gms.analytics.u.a("BLANK_23", 23);
            ug = new com.google.android.gms.analytics.u.a("BLANK_24", 24);
            uh = new com.google.android.gms.analytics.u.a("BLANK_25", 25);
            ui = new com.google.android.gms.analytics.u.a("BLANK_26", 26);
            uj = new com.google.android.gms.analytics.u.a("BLANK_27", 27);
            uk = new com.google.android.gms.analytics.u.a("BLANK_28", 28);
            ul = new com.google.android.gms.analytics.u.a("BLANK_29", 29);
            um = new com.google.android.gms.analytics.u.a("SET_EXCEPTION_PARSER", 30);
            un = new com.google.android.gms.analytics.u.a("GET_EXCEPTION_PARSER", 31);
            uo = new com.google.android.gms.analytics.u.a("CONSTRUCT_TRANSACTION", 32);
            up = new com.google.android.gms.analytics.u.a("CONSTRUCT_EXCEPTION", 33);
            uq = new com.google.android.gms.analytics.u.a("CONSTRUCT_RAW_EXCEPTION", 34);
            ur = new com.google.android.gms.analytics.u.a("CONSTRUCT_TIMING", 35);
            us = new com.google.android.gms.analytics.u.a("CONSTRUCT_SOCIAL", 36);
            ut = new com.google.android.gms.analytics.u.a("BLANK_37", 37);
            uu = new com.google.android.gms.analytics.u.a("BLANK_38", 38);
            uv = new com.google.android.gms.analytics.u.a("GET_TRACKER", 39);
            uw = new com.google.android.gms.analytics.u.a("GET_DEFAULT_TRACKER", 40);
            ux = new com.google.android.gms.analytics.u.a("SET_DEFAULT_TRACKER", 41);
            uy = new com.google.android.gms.analytics.u.a("SET_APP_OPT_OUT", 42);
            uz = new com.google.android.gms.analytics.u.a("GET_APP_OPT_OUT", 43);
            uA = new com.google.android.gms.analytics.u.a("DISPATCH", 44);
            uB = new com.google.android.gms.analytics.u.a("SET_DISPATCH_PERIOD", 45);
            uC = new com.google.android.gms.analytics.u.a("BLANK_46", 46);
            uD = new com.google.android.gms.analytics.u.a("REPORT_UNCAUGHT_EXCEPTIONS", 47);
            uE = new com.google.android.gms.analytics.u.a("SET_AUTO_ACTIVITY_TRACKING", 48);
            uF = new com.google.android.gms.analytics.u.a("SET_SESSION_TIMEOUT", 49);
            uG = new com.google.android.gms.analytics.u.a("CONSTRUCT_EVENT", 50);
            uH = new com.google.android.gms.analytics.u.a("CONSTRUCT_ITEM", 51);
            uI = new com.google.android.gms.analytics.u.a("BLANK_52", 52);
            uJ = new com.google.android.gms.analytics.u.a("BLANK_53", 53);
            uK = new com.google.android.gms.analytics.u.a("SET_DRY_RUN", 54);
            uL = new com.google.android.gms.analytics.u.a("GET_DRY_RUN", 55);
            uM = new com.google.android.gms.analytics.u.a("SET_LOGGER", 56);
            uN = new com.google.android.gms.analytics.u.a("SET_FORCE_LOCAL_DISPATCH", 57);
            uO = new com.google.android.gms.analytics.u.a("GET_TRACKER_NAME", 58);
            uP = new com.google.android.gms.analytics.u.a("CLOSE_TRACKER", 59);
            uQ = new com.google.android.gms.analytics.u.a("EASY_TRACKER_ACTIVITY_START", 60);
            uR = new com.google.android.gms.analytics.u.a("EASY_TRACKER_ACTIVITY_STOP", 61);
            uS = new com.google.android.gms.analytics.u.a("CONSTRUCT_APP_VIEW", 62);
            uT = new com.google.android.gms.analytics.u.a[]{tI, tJ, tK, tL, tM, tN, tO, tP, tQ, tR, tS, tT, tU, tV, tW, tX, tY, tZ, ua, ub, uc, ud, ue, uf, ug, uh, ui, uj, uk, ul, um, un, uo, up, uq, ur, us, ut, uu, uv, uw, ux, uy, uz, uA, uB, uC, uD, uE, uF, uG, uH, uI, uJ, uK, uL, uM, uN, uO, uP, uQ, uR, uS};
        }
    }

    static {
        tH = new u();
    }

    private u() {
        this.tE = new TreeSet();
        this.tF = new StringBuilder();
        this.tG = false;
    }

    public static u cy() {
        return tH;
    }

    public synchronized void a(a aVar) {
        if (!this.tG) {
            this.tE.add(aVar);
            this.tF.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_".charAt(aVar.ordinal()));
        }
    }

    public synchronized String cA() {
        String toString;
        if (this.tF.length() > 0) {
            this.tF.insert(0, ".");
        }
        toString = this.tF.toString();
        this.tF = new StringBuilder();
        return toString;
    }

    public synchronized String cz() {
        StringBuilder stringBuilder;
        stringBuilder = new StringBuilder();
        int i = ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES;
        int i2 = 0;
        while (this.tE.size() > 0) {
            a aVar = (a) this.tE.first();
            this.tE.remove(aVar);
            int ordinal = aVar.ordinal();
            while (ordinal >= i) {
                stringBuilder.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_".charAt(i2));
                i += 6;
                i2 = 0;
            }
            i2 += 1 << (aVar.ordinal() % 6);
        }
        if (i2 > 0 || stringBuilder.length() == 0) {
            stringBuilder.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_".charAt(i2));
        }
        this.tE.clear();
        return stringBuilder.toString();
    }

    public synchronized void t(boolean z) {
        this.tG = z;
    }
}