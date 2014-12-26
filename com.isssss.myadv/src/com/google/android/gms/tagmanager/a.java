package com.google.android.gms.tagmanager;

import android.content.Context;
import android.os.Process;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.internal.gl;
import com.google.android.gms.internal.gn;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import java.io.IOException;

class a {
    private static a Wx;
    private static Object sf;
    private volatile long Ws;
    private volatile long Wt;
    private volatile long Wu;
    private final gl Wv;
    private a Ww;
    private volatile boolean mClosed;
    private final Context mContext;
    private final Thread qY;
    private volatile Info sh;

    public static interface a {
        Info jW();
    }

    static {
        sf = new Object();
    }

    private a(Context context) {
        this(context, null, gn.ft());
    }

    a(Context context, a aVar, gl glVar) {
        this.Ws = 900000;
        this.Wt = 30000;
        this.mClosed = false;
        this.Ww = new a() {
            public Info jW() {
                Info info = null;
                try {
                    return AdvertisingIdClient.getAdvertisingIdInfo(a.this.mContext);
                } catch (IllegalStateException e) {
                    bh.z("IllegalStateException getting Advertising Id Info");
                    return info;
                } catch (GooglePlayServicesRepairableException e2) {
                    bh.z("GooglePlayServicesRepairableException getting Advertising Id Info");
                    return info;
                } catch (IOException e3) {
                    bh.z("IOException getting Ad Id Info");
                    return info;
                } catch (GooglePlayServicesNotAvailableException e4) {
                    bh.z("GooglePlayServicesNotAvailableException getting Advertising Id Info");
                    return info;
                } catch (Exception e5) {
                    bh.z("Unknown exception. Could not get the Advertising Id Info.");
                    return info;
                }
            }
        };
        this.Wv = glVar;
        if (context != null) {
            this.mContext = context.getApplicationContext();
        } else {
            this.mContext = context;
        }
        if (aVar != null) {
            this.Ww = aVar;
        }
        this.qY = new Thread(new Runnable() {
            public void run() {
                a.this.jU();
            }
        });
    }

    static a E(Context context) {
        if (Wx == null) {
            synchronized (sf) {
                if (Wx == null) {
                    Wx = new a(context);
                    Wx.start();
                }
            }
        }
        return Wx;
    }

    private void jU() {
        Process.setThreadPriority(ApiEventType.API_MRAID_USE_CUSTOM_CLOSE);
        while (!this.mClosed) {
            try {
                this.sh = this.Ww.jW();
                Thread.sleep(this.Ws);
            } catch (InterruptedException e) {
                bh.x("sleep interrupted in AdvertiserDataPoller thread; continuing");
            }
        }
    }

    private void jV() {
        if (this.Wv.currentTimeMillis() - this.Wu >= this.Wt) {
            interrupt();
            this.Wu = this.Wv.currentTimeMillis();
        }
    }

    void interrupt() {
        this.qY.interrupt();
    }

    public boolean isLimitAdTrackingEnabled() {
        jV();
        return this.sh == null ? true : this.sh.isLimitAdTrackingEnabled();
    }

    public String jT() {
        jV();
        return this.sh == null ? null : this.sh.getId();
    }

    void start() {
        this.qY.start();
    }
}