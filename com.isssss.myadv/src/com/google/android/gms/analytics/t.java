package com.google.android.gms.analytics;

import android.content.Context;
import android.os.Process;
import android.support.v4.os.EnvironmentCompat;
import android.text.TextUtils;
import com.google.android.gms.internal.ef;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.mopub.mobileads.factories.HttpClientFactory;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

class t extends Thread implements f {
    private static t tA;
    private volatile boolean mClosed;
    private final Context mContext;
    private volatile String su;
    private volatile ag tB;
    private final LinkedBlockingQueue<Runnable> tw;
    private volatile boolean tx;
    private volatile List<ef> ty;
    private volatile String tz;

    class AnonymousClass_1 implements Runnable {
        final /* synthetic */ Map tC;

        AnonymousClass_1(Map map) {
            this.tC = map;
        }

        public void run() {
            if (TextUtils.isEmpty((CharSequence) this.tC.get("&cid"))) {
                this.tC.put("&cid", t.this.su);
            }
            if (!GoogleAnalytics.getInstance(t.this.mContext).getAppOptOut() && !t.this.s(this.tC)) {
                if (!TextUtils.isEmpty(t.this.tz)) {
                    u.cy().t(true);
                    this.tC.putAll(new HitBuilder().setCampaignParamsFromUrl(t.this.tz).build());
                    u.cy().t(false);
                    t.this.tz = null;
                }
                t.this.u(this.tC);
                t.this.t(this.tC);
                t.this.tB.b(y.v(this.tC), Long.valueOf((String) this.tC.get("&ht")).longValue(), t.this.r(this.tC), t.this.ty);
            }
        }
    }

    private t(Context context) {
        super("GAThread");
        this.tw = new LinkedBlockingQueue();
        this.tx = false;
        this.mClosed = false;
        if (context != null) {
            this.mContext = context.getApplicationContext();
        } else {
            this.mContext = context;
        }
        start();
    }

    static int H(String str) {
        int i = 1;
        if (!TextUtils.isEmpty(str)) {
            i = 0;
            int i2 = str.length() - 1;
            while (i2 >= 0) {
                char charAt = str.charAt(i2);
                i = (i << 6) & 268435455 + charAt + charAt << 14;
                int i3 = 266338304 & i;
                if (i3 != 0) {
                    i ^= i3 >> 21;
                }
                i2--;
            }
        }
        return i;
    }

    private String a(Throwable th) {
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteArrayOutputStream);
        th.printStackTrace(printStream);
        printStream.flush();
        return new String(byteArrayOutputStream.toByteArray());
    }

    static t q(Context context) {
        if (tA == null) {
            tA = new t(context);
        }
        return tA;
    }

    static String r(Context context) {
        String str = null;
        try {
            FileInputStream openFileInput = context.openFileInput("gaInstallData");
            byte[] bArr = new byte[8192];
            int read = openFileInput.read(bArr, 0, HttpClientFactory.SOCKET_SIZE);
            if (openFileInput.available() > 0) {
                aa.w("Too much campaign data, ignoring it.");
                openFileInput.close();
                context.deleteFile("gaInstallData");
                return str;
            } else {
                openFileInput.close();
                context.deleteFile("gaInstallData");
                if (read <= 0) {
                    aa.z("Campaign file is empty.");
                    return str;
                } else {
                    String str2 = new String(bArr, 0, read);
                    aa.x("Campaign found: " + str2);
                    return str2;
                }
            }
        } catch (FileNotFoundException e) {
            aa.x("No campaign data found.");
            return str;
        } catch (IOException e2) {
            aa.w("Error reading campaign data.");
            context.deleteFile("gaInstallData");
            return str;
        }
    }

    private String r(Map<String, String> map) {
        return map.containsKey("useSecure") ? ak.d((String) map.get("useSecure"), true) ? "https:" : "http:" : "https:";
    }

    private boolean s(Map<String, String> map) {
        if (map.get("&sf") == null) {
            return false;
        }
        double a = ak.a((String) map.get("&sf"), 100.0d);
        if (a >= 100.0d) {
            return false;
        }
        if (((double) (H((String) map.get("&cid")) % 10000)) < a * 100.0d) {
            return false;
        }
        String str = map.get("&t") == null ? EnvironmentCompat.MEDIA_UNKNOWN : (String) map.get("&t");
        aa.y(String.format("%s hit sampled out", new Object[]{str}));
        return true;
    }

    private void t(Map<String, String> map) {
        m m = a.m(this.mContext);
        ak.a(map, "&adid", m.getValue("&adid"));
        ak.a(map, "&ate", m.getValue("&ate"));
    }

    private void u(Map<String, String> map) {
        m ca = g.ca();
        ak.a(map, "&an", ca.getValue("&an"));
        ak.a(map, "&av", ca.getValue("&av"));
        ak.a(map, "&aid", ca.getValue("&aid"));
        ak.a(map, "&aiid", ca.getValue("&aiid"));
        map.put("&v", "1");
    }

    void a(Runnable runnable) {
        this.tw.add(runnable);
    }

    public void bR() {
        a(new Runnable() {
            public void run() {
                t.this.tB.bR();
            }
        });
    }

    public void bW() {
        a(new Runnable() {
            public void run() {
                t.this.tB.bW();
            }
        });
    }

    public void bY() {
        a(new Runnable() {
            public void run() {
                t.this.tB.bY();
            }
        });
    }

    public LinkedBlockingQueue<Runnable> bZ() {
        return this.tw;
    }

    public Thread getThread() {
        return this;
    }

    protected void init() {
        this.tB.cp();
        this.ty = new ArrayList();
        this.ty.add(new ef("appendVersion", "&_v".substring(1), "ma4.0.1"));
        this.ty.add(new ef("appendQueueTime", "&qt".substring(1), null));
        this.ty.add(new ef("appendCacheBuster", "&z".substring(1), null));
    }

    public void q(Map<String, String> map) {
        Map hashMap = new HashMap(map);
        String str = (String) map.get("&ht");
        if (str != null) {
            try {
                Long.valueOf(str);
            } catch (NumberFormatException e) {
                str = null;
            }
        }
        if (str == null) {
            hashMap.put("&ht", Long.toString(System.currentTimeMillis()));
        }
        a(new AnonymousClass_1(hashMap));
    }

    public void run() {
        Process.setThreadPriority(ApiEventType.API_MRAID_USE_CUSTOM_CLOSE);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            aa.z("sleep interrupted in GAThread initialize");
        }
        try {
            if (this.tB == null) {
                this.tB = new s(this.mContext, this);
            }
            init();
            this.su = h.cb().getValue("&cid");
            if (this.su == null) {
                this.tx = true;
            }
            this.tz = r(this.mContext);
            aa.y("Initialized GA Thread");
        } catch (Throwable th) {
            aa.w("Error initializing the GAThread: " + a(th));
            aa.w("Google Analytics will not start up.");
            this.tx = true;
        }
        while (!this.mClosed) {
            try {
                Runnable runnable = (Runnable) this.tw.take();
                if (!this.tx) {
                    runnable.run();
                }
            } catch (InterruptedException e2) {
                try {
                    aa.x(e2.toString());
                } catch (Throwable th2) {
                    aa.w("Error on GAThread: " + a(th2));
                    aa.w("Google Analytics is shutting down.");
                    this.tx = true;
                }
            }
        }
    }
}