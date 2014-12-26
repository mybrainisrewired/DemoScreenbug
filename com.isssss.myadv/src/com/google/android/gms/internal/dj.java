package com.google.android.gms.internal;

import android.os.Bundle;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.UUID;

public class dj {
    private static final dj qJ;
    public static final String qK;
    private final Object li;
    public final String qL;
    private final dk qM;
    private BigInteger qN;
    private final HashSet<di> qO;
    private final HashMap<String, dm> qP;

    static {
        qJ = new dj();
        qK = qJ.qL;
    }

    private dj() {
        this.li = new Object();
        this.qN = BigInteger.ONE;
        this.qO = new HashSet();
        this.qP = new HashMap();
        this.qL = br();
        this.qM = new dk(this.qL);
    }

    public static Bundle a(dl dlVar, String str) {
        return qJ.b(dlVar, str);
    }

    public static void b(HashSet<di> hashSet) {
        qJ.c(hashSet);
    }

    public static dj bq() {
        return qJ;
    }

    private static String br() {
        UUID randomUUID = UUID.randomUUID();
        byte[] toByteArray = BigInteger.valueOf(randomUUID.getLeastSignificantBits()).toByteArray();
        byte[] toByteArray2 = BigInteger.valueOf(randomUUID.getMostSignificantBits()).toByteArray();
        String str = new BigInteger(1, toByteArray).toString();
        int i = 0;
        while (i < 2) {
            try {
                MessageDigest instance = MessageDigest.getInstance("MD5");
                instance.update(toByteArray);
                instance.update(toByteArray2);
                Object obj = new Object[8];
                System.arraycopy(instance.digest(), 0, obj, 0, ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES);
                str = new BigInteger(1, obj).toString();
            } catch (NoSuchAlgorithmException e) {
            }
            i++;
        }
        return str;
    }

    public static String bs() {
        return qJ.bt();
    }

    public static dk bu() {
        return qJ.bv();
    }

    public void a(di diVar) {
        synchronized (this.li) {
            this.qO.add(diVar);
        }
    }

    public void a(String str, dm dmVar) {
        synchronized (this.li) {
            this.qP.put(str, dmVar);
        }
    }

    public Bundle b(dl dlVar, String str) {
        Bundle bundle;
        synchronized (this.li) {
            bundle = new Bundle();
            bundle.putBundle("app", this.qM.q(str));
            Bundle bundle2 = new Bundle();
            Iterator it = this.qP.keySet().iterator();
            while (it.hasNext()) {
                String str2 = (String) it.next();
                bundle2.putBundle(str2, ((dm) this.qP.get(str2)).toBundle());
            }
            bundle.putBundle("slots", bundle2);
            ArrayList arrayList = new ArrayList();
            Iterator it2 = this.qO.iterator();
            while (it2.hasNext()) {
                arrayList.add(((di) it2.next()).toBundle());
            }
            bundle.putParcelableArrayList("ads", arrayList);
            dlVar.a(this.qO);
            this.qO.clear();
        }
        return bundle;
    }

    public String bt() {
        String toString;
        synchronized (this.li) {
            toString = this.qN.toString();
            this.qN = this.qN.add(BigInteger.ONE);
        }
        return toString;
    }

    public dk bv() {
        dk dkVar;
        synchronized (this.li) {
            dkVar = this.qM;
        }
        return dkVar;
    }

    public void c(HashSet<di> hashSet) {
        synchronized (this.li) {
            this.qO.addAll(hashSet);
        }
    }
}