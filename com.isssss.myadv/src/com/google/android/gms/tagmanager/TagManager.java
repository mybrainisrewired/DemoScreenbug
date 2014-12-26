package com.google.android.gms.tagmanager;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import com.google.android.gms.common.api.PendingResult;
import com.inmobi.commons.analytics.iat.impl.AdTrackerConstants;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TagManager {
    private static TagManager aay;
    private final DataLayer WK;
    private final r Zg;
    private final a aaw;
    private final ConcurrentMap<n, Boolean> aax;
    private final Context mContext;

    static /* synthetic */ class AnonymousClass_3 {
        static final /* synthetic */ int[] aaA;

        static {
            aaA = new int[a.values().length];
            try {
                aaA[a.YT.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                aaA[a.YU.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            aaA[a.YV.ordinal()] = 3;
        }
    }

    static interface a {
        o a(Context context, TagManager tagManager, Looper looper, String str, int i, r rVar);
    }

    TagManager(Context context, a containerHolderLoaderProvider, DataLayer dataLayer) {
        if (context == null) {
            throw new NullPointerException(AdTrackerConstants.MSG_APP_CONTEXT_NULL);
        }
        this.mContext = context.getApplicationContext();
        this.aaw = containerHolderLoaderProvider;
        this.aax = new ConcurrentHashMap();
        this.WK = dataLayer;
        this.WK.a(new b() {
            public void y(Map<String, Object> map) {
                Object obj = map.get(DataLayer.EVENT_KEY);
                if (obj != null) {
                    TagManager.this.bT(obj.toString());
                }
            }
        });
        this.WK.a(new d(this.mContext));
        this.Zg = new r();
    }

    private void bT(String str) {
        Iterator it = this.aax.keySet().iterator();
        while (it.hasNext()) {
            ((n) it.next()).bp(str);
        }
    }

    public static TagManager getInstance(Context context) {
        TagManager tagManager;
        synchronized (TagManager.class) {
            if (aay == null && context == null) {
                bh.w("TagManager.getInstance requires non-null context.");
                throw new NullPointerException();
            } else {
                aay = new TagManager(context, new a() {
                    public o a(Context context, TagManager tagManager, Looper looper, String str, int i, r rVar) {
                        return new o(context, tagManager, looper, str, i, rVar);
                    }
                }, new DataLayer(new v(context)));
            }
            tagManager = aay;
        }
        return tagManager;
    }

    void a(n nVar) {
        this.aax.put(nVar, Boolean.valueOf(true));
    }

    boolean b(n nVar) {
        return this.aax.remove(nVar) != null;
    }

    synchronized boolean g(Uri uri) {
        boolean z;
        cd kT = cd.kT();
        if (kT.g(uri)) {
            String containerId = kT.getContainerId();
            n nVar;
            switch (AnonymousClass_3.aaA[kT.kU().ordinal()]) {
                case MMAdView.TRANSITION_FADE:
                    Iterator it = this.aax.keySet().iterator();
                    while (it.hasNext()) {
                        nVar = (n) it.next();
                        if (nVar.getContainerId().equals(containerId)) {
                            nVar.br(null);
                            nVar.refresh();
                        }
                    }
                    break;
                case MMAdView.TRANSITION_UP:
                case MMAdView.TRANSITION_DOWN:
                    Iterator it2 = this.aax.keySet().iterator();
                    while (it2.hasNext()) {
                        nVar = (n) it2.next();
                        if (nVar.getContainerId().equals(containerId)) {
                            nVar.br(kT.kV());
                            nVar.refresh();
                        } else if (nVar.ke() != null) {
                            nVar.br(null);
                            nVar.refresh();
                        }
                    }
                    break;
            }
            z = true;
        } else {
            z = false;
        }
        return z;
    }

    public DataLayer getDataLayer() {
        return this.WK;
    }

    public PendingResult<ContainerHolder> loadContainerDefaultOnly(String containerId, int defaultContainerResourceId) {
        PendingResult a = this.aaw.a(this.mContext, this, null, containerId, defaultContainerResourceId, this.Zg);
        a.kh();
        return a;
    }

    public PendingResult<ContainerHolder> loadContainerDefaultOnly(String containerId, int defaultContainerResourceId, Handler handler) {
        PendingResult a = this.aaw.a(this.mContext, this, handler.getLooper(), containerId, defaultContainerResourceId, this.Zg);
        a.kh();
        return a;
    }

    public PendingResult<ContainerHolder> loadContainerPreferFresh(String containerId, int defaultContainerResourceId) {
        PendingResult a = this.aaw.a(this.mContext, this, null, containerId, defaultContainerResourceId, this.Zg);
        a.kj();
        return a;
    }

    public PendingResult<ContainerHolder> loadContainerPreferFresh(String containerId, int defaultContainerResourceId, Handler handler) {
        PendingResult a = this.aaw.a(this.mContext, this, handler.getLooper(), containerId, defaultContainerResourceId, this.Zg);
        a.kj();
        return a;
    }

    public PendingResult<ContainerHolder> loadContainerPreferNonDefault(String containerId, int defaultContainerResourceId) {
        PendingResult a = this.aaw.a(this.mContext, this, null, containerId, defaultContainerResourceId, this.Zg);
        a.ki();
        return a;
    }

    public PendingResult<ContainerHolder> loadContainerPreferNonDefault(String containerId, int defaultContainerResourceId, Handler handler) {
        PendingResult a = this.aaw.a(this.mContext, this, handler.getLooper(), containerId, defaultContainerResourceId, this.Zg);
        a.ki();
        return a;
    }

    public void setVerboseLoggingEnabled(boolean enableVerboseLogging) {
        bh.setLogLevel(enableVerboseLogging ? MMAdView.TRANSITION_UP : ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES);
    }
}