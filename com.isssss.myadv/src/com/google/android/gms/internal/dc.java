package com.google.android.gms.internal;

import android.content.Context;
import android.location.Location;
import android.os.SystemClock;
import android.text.TextUtils;
import com.google.android.gms.internal.db.a;
import com.millennialmedia.android.MMAdView;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class dc extends a {
    private static final Object px;
    private static dc py;
    private final Context mContext;
    private final ax pA;
    private final bf pz;

    static class AnonymousClass_1 implements Runnable {
        final /* synthetic */ Context pB;
        final /* synthetic */ cx pC;
        final /* synthetic */ de pD;
        final /* synthetic */ ea.a pE;
        final /* synthetic */ String pF;

        AnonymousClass_1(Context context, cx cxVar, de deVar, ea.a aVar, String str) {
            this.pB = context;
            this.pC = cxVar;
            this.pD = deVar;
            this.pE = aVar;
            this.pF = str;
        }

        public void run() {
            dz a = dz.a(this.pB, new ak(), false, false, null, this.pC.kK);
            a.setWillNotDraw(true);
            this.pD.b(a);
            ea bI = a.bI();
            bI.a("/invalidRequest", this.pD.pK);
            bI.a("/loadAdURL", this.pD.pL);
            bI.a("/log", ba.mM);
            bI.a(this.pE);
            dw.v("Loading the JS library.");
            a.loadUrl(this.pF);
        }
    }

    static class AnonymousClass_2 implements ea.a {
        final /* synthetic */ String pG;

        AnonymousClass_2(String str) {
            this.pG = str;
        }

        public void a(dz dzVar) {
            String format = String.format("javascript:%s(%s);", new Object[]{"AFMA_buildAdURL", this.pG});
            dw.y("About to execute: " + format);
            dzVar.loadUrl(format);
        }
    }

    static {
        px = new Object();
    }

    private dc(Context context, ax axVar, bf bfVar) {
        this.mContext = context;
        this.pz = bfVar;
        this.pA = axVar;
    }

    private static cz a(Context context, ax axVar, bf bfVar, cx cxVar) {
        dw.v("Starting ad request from service.");
        bfVar.init();
        dg dgVar = new dg(context);
        if (dgVar.qk == -1) {
            dw.v("Device is offline.");
            return new cz(2);
        } else {
            de deVar = new de(cxVar.applicationInfo.packageName);
            if (cxVar.pg.extras != null) {
                String string = cxVar.pg.extras.getString("_ad");
                if (string != null) {
                    return dd.a(context, cxVar, string);
                }
            }
            Location a = bfVar.a(250);
            String aH = axVar.aH();
            String a2 = dd.a(cxVar, dgVar, a, axVar.aI());
            if (a2 == null) {
                return new cz(0);
            }
            dv.rp.post(new AnonymousClass_1(context, cxVar, deVar, p(a2), aH));
            a2 = deVar.bj();
            return TextUtils.isEmpty(a2) ? new cz(deVar.getErrorCode()) : a(context, cxVar.kK.rq, a2);
        }
    }

    public static cz a(Context context, String str, String str2) {
        try {
            df dfVar = new df();
            URL url = new URL(str2);
            long elapsedRealtime = SystemClock.elapsedRealtime();
            URL url2 = url;
            int i = 0;
            while (true) {
                HttpURLConnection httpURLConnection = (HttpURLConnection) url2.openConnection();
                dq.a(context, str, false, httpURLConnection);
                int responseCode = httpURLConnection.getResponseCode();
                Map headerFields = httpURLConnection.getHeaderFields();
                cz czVar;
                if (responseCode < 200 || responseCode >= 300) {
                    a(url2.toString(), headerFields, null, responseCode);
                    if (responseCode < 300 || responseCode >= 400) {
                        dw.z("Received error HTTP response code: " + responseCode);
                        czVar = new cz(0);
                        httpURLConnection.disconnect();
                        return czVar;
                    } else {
                        Object headerField = httpURLConnection.getHeaderField("Location");
                        if (TextUtils.isEmpty(headerField)) {
                            dw.z("No location header to follow redirect.");
                            czVar = new cz(0);
                            httpURLConnection.disconnect();
                            return czVar;
                        } else {
                            url2 = new URL(headerField);
                            i++;
                            if (i > 5) {
                                dw.z("Too many redirects.");
                                czVar = new cz(0);
                                httpURLConnection.disconnect();
                                return czVar;
                            } else {
                                dfVar.e(headerFields);
                                httpURLConnection.disconnect();
                            }
                        }
                    }
                } else {
                    String toString = url2.toString();
                    String a = dq.a(new InputStreamReader(httpURLConnection.getInputStream()));
                    a(toString, headerFields, a, responseCode);
                    dfVar.a(toString, headerFields, a);
                    czVar = dfVar.g(elapsedRealtime);
                    httpURLConnection.disconnect();
                    return czVar;
                }
            }
        } catch (IOException e) {
            dw.z("Error while connecting to ad server: " + e.getMessage());
            return new cz(2);
        }
    }

    public static dc a(Context context, ax axVar, bf bfVar) {
        dc dcVar;
        synchronized (px) {
            if (py == null) {
                py = new dc(context.getApplicationContext(), axVar, bfVar);
            }
            dcVar = py;
        }
        return dcVar;
    }

    private static void a(String str, Map<String, List<String>> map, String str2, int i) {
        if (dw.n(MMAdView.TRANSITION_UP)) {
            dw.y("Http Response: {\n  URL:\n    " + str + "\n  Headers:");
            if (map != null) {
                Iterator it = map.keySet().iterator();
                while (it.hasNext()) {
                    String str3 = (String) it.next();
                    dw.y("    " + str3 + ":");
                    Iterator it2 = ((List) map.get(str3)).iterator();
                    while (it2.hasNext()) {
                        dw.y("      " + ((String) it2.next()));
                    }
                }
            }
            dw.y("  Body:");
            if (str2 != null) {
                int i2 = 0;
                while (i2 < Math.min(str2.length(), 100000)) {
                    dw.y(str2.substring(i2, Math.min(str2.length(), i2 + 1000)));
                    i2 += 1000;
                }
            } else {
                dw.y("    null");
            }
            dw.y("  Response Code:\n    " + i + "\n}");
        }
    }

    private static ea.a p(String str) {
        return new AnonymousClass_2(str);
    }

    public cz b(cx cxVar) {
        return a(this.mContext, this.pA, this.pz, cxVar);
    }
}