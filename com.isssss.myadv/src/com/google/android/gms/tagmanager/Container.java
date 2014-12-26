package com.google.android.gms.tagmanager;

import android.content.Context;
import com.google.android.gms.internal.c.f;
import com.google.android.gms.internal.c.i;
import com.google.android.gms.internal.c.j;
import com.google.android.gms.tagmanager.cq.c;
import com.google.android.gms.tagmanager.cq.g;
import com.mopub.common.Preconditions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Container {
    private final String WJ;
    private final DataLayer WK;
    private cs WL;
    private Map<String, FunctionCallMacroCallback> WM;
    private Map<String, FunctionCallTagCallback> WN;
    private volatile long WO;
    private volatile String WP;
    private final Context mContext;

    public static interface FunctionCallMacroCallback {
        Object getValue(String str, Map<String, Object> map);
    }

    public static interface FunctionCallTagCallback {
        void execute(String str, Map<String, Object> map);
    }

    private class a implements com.google.android.gms.tagmanager.s.a {
        private a() {
        }

        public Object b(String str, Map<String, Object> map) {
            com.google.android.gms.tagmanager.Container.FunctionCallMacroCallback bn = Container.this.bn(str);
            return bn == null ? null : bn.getValue(str, map);
        }
    }

    private class b implements com.google.android.gms.tagmanager.s.a {
        private b() {
        }

        public Object b(String str, Map<String, Object> map) {
            com.google.android.gms.tagmanager.Container.FunctionCallTagCallback bo = Container.this.bo(str);
            if (bo != null) {
                bo.execute(str, map);
            }
            return dh.lS();
        }
    }

    Container(Context context, DataLayer dataLayer, String containerId, long lastRefreshTime, j resource) {
        this.WM = new HashMap();
        this.WN = new HashMap();
        this.WP = Preconditions.EMPTY_ARGUMENTS;
        this.mContext = context;
        this.WK = dataLayer;
        this.WJ = containerId;
        this.WO = lastRefreshTime;
        a(resource.fK);
        if (resource.fJ != null) {
            a(resource.fJ);
        }
    }

    Container(Context context, DataLayer dataLayer, String containerId, long lastRefreshTime, c resource) {
        this.WM = new HashMap();
        this.WN = new HashMap();
        this.WP = Preconditions.EMPTY_ARGUMENTS;
        this.mContext = context;
        this.WK = dataLayer;
        this.WJ = containerId;
        this.WO = lastRefreshTime;
        a(resource);
    }

    private void a(f fVar) {
        if (fVar == null) {
            throw new NullPointerException();
        }
        try {
            a(cq.b(fVar));
        } catch (g e) {
            bh.w("Not loading resource: " + fVar + " because it is invalid: " + e.toString());
        }
    }

    private void a(c cVar) {
        this.WP = cVar.getVersion();
        c cVar2 = cVar;
        a(new cs(this.mContext, cVar2, this.WK, new a(null), new b(null), bq(this.WP)));
    }

    private synchronized void a(cs csVar) {
        this.WL = csVar;
    }

    private void a(i[] iVarArr) {
        List arrayList = new ArrayList();
        int length = iVarArr.length;
        int i = 0;
        while (i < length) {
            arrayList.add(iVarArr[i]);
            i++;
        }
        kd().e(arrayList);
    }

    private synchronized cs kd() {
        return this.WL;
    }

    FunctionCallMacroCallback bn(String str) {
        FunctionCallMacroCallback functionCallMacroCallback;
        synchronized (this.WM) {
            functionCallMacroCallback = (FunctionCallMacroCallback) this.WM.get(str);
        }
        return functionCallMacroCallback;
    }

    FunctionCallTagCallback bo(String str) {
        FunctionCallTagCallback functionCallTagCallback;
        synchronized (this.WN) {
            functionCallTagCallback = (FunctionCallTagCallback) this.WN.get(str);
        }
        return functionCallTagCallback;
    }

    void bp(String str) {
        kd().bp(str);
    }

    ag bq(String str) {
        if (cd.kT().kU().equals(a.YV)) {
        }
        return new bq();
    }

    public boolean getBoolean(String key) {
        cs kd = kd();
        if (kd == null) {
            bh.w("getBoolean called for closed container.");
            return dh.lQ().booleanValue();
        } else {
            try {
                return dh.n((com.google.android.gms.internal.d.a) kd.bR(key).getObject()).booleanValue();
            } catch (Exception e) {
                bh.w("Calling getBoolean() threw an exception: " + e.getMessage() + " Returning default value.");
                return dh.lQ().booleanValue();
            }
        }
    }

    public String getContainerId() {
        return this.WJ;
    }

    public double getDouble(String key) {
        cs kd = kd();
        if (kd == null) {
            bh.w("getDouble called for closed container.");
            return dh.lP().doubleValue();
        } else {
            try {
                return dh.m((com.google.android.gms.internal.d.a) kd.bR(key).getObject()).doubleValue();
            } catch (Exception e) {
                bh.w("Calling getDouble() threw an exception: " + e.getMessage() + " Returning default value.");
                return dh.lP().doubleValue();
            }
        }
    }

    public long getLastRefreshTime() {
        return this.WO;
    }

    public long getLong(String key) {
        cs kd = kd();
        if (kd == null) {
            bh.w("getLong called for closed container.");
            return dh.lO().longValue();
        } else {
            try {
                return dh.l((com.google.android.gms.internal.d.a) kd.bR(key).getObject()).longValue();
            } catch (Exception e) {
                bh.w("Calling getLong() threw an exception: " + e.getMessage() + " Returning default value.");
                return dh.lO().longValue();
            }
        }
    }

    public String getString(String key) {
        cs kd = kd();
        if (kd == null) {
            bh.w("getString called for closed container.");
            return dh.lS();
        } else {
            try {
                return dh.j((com.google.android.gms.internal.d.a) kd.bR(key).getObject());
            } catch (Exception e) {
                bh.w("Calling getString() threw an exception: " + e.getMessage() + " Returning default value.");
                return dh.lS();
            }
        }
    }

    public boolean isDefault() {
        return getLastRefreshTime() == 0;
    }

    String kc() {
        return this.WP;
    }

    public void registerFunctionCallMacroCallback(String customMacroName, FunctionCallMacroCallback customMacroCallback) {
        if (customMacroCallback == null) {
            throw new NullPointerException("Macro handler must be non-null");
        }
        synchronized (this.WM) {
            this.WM.put(customMacroName, customMacroCallback);
        }
    }

    public void registerFunctionCallTagCallback(String customTagName, FunctionCallTagCallback customTagCallback) {
        if (customTagCallback == null) {
            throw new NullPointerException("Tag callback must be non-null");
        }
        synchronized (this.WN) {
            this.WN.put(customTagName, customTagCallback);
        }
    }

    void release() {
        this.WL = null;
    }

    public void unregisterFunctionCallMacroCallback(String customMacroName) {
        synchronized (this.WM) {
            this.WM.remove(customMacroName);
        }
    }

    public void unregisterFunctionCallTagCallback(String customTagName) {
        synchronized (this.WN) {
            this.WN.remove(customTagName);
        }
    }
}