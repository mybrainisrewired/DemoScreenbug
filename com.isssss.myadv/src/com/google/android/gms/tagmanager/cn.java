package com.google.android.gms.tagmanager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.google.android.gms.internal.c.j;
import com.google.android.gms.tagmanager.bg.a;
import com.mopub.common.Preconditions;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class cn implements Runnable {
    private final String WJ;
    private volatile String Xg;
    private final bm Zd;
    private final String Ze;
    private bg<j> Zf;
    private volatile r Zg;
    private volatile String Zh;
    private final Context mContext;

    cn(Context context, String str, bm bmVar, r rVar) {
        this.mContext = context;
        this.Zd = bmVar;
        this.WJ = str;
        this.Zg = rVar;
        this.Ze = "/r?id=" + str;
        this.Xg = this.Ze;
        this.Zh = null;
    }

    public cn(Context context, String str, r rVar) {
        this(context, str, new bm(), rVar);
    }

    private boolean kW() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) this.mContext.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            return true;
        }
        bh.y("...no network connectivity");
        return false;
    }

    private void kX() {
        Throwable th;
        if (kW()) {
            bh.y("Start loading resource from network ...");
            String kY = kY();
            bl kH = this.Zd.kH();
            try {
                InputStream bD = kH.bD(kY);
                try {
                    OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    cq.b(bD, byteArrayOutputStream);
                    j b = j.b(byteArrayOutputStream.toByteArray());
                    bh.y("Successfully loaded supplemented resource: " + b);
                    if (b.fK == null && b.fJ.length == 0) {
                        bh.y("No change for container: " + this.WJ);
                    }
                    this.Zf.i(b);
                    kH.close();
                    bh.y("Load resource from network finished.");
                } catch (IOException e) {
                    th = e;
                    bh.c("Error when parsing downloaded resources from url: " + kY + " " + th.getMessage(), th);
                    this.Zf.a(a.YA);
                    kH.close();
                }
            } catch (FileNotFoundException e2) {
                bh.z("No data is retrieved from the given url: " + kY + ". Make sure container_id: " + this.WJ + " is correct.");
                this.Zf.a(a.YA);
                kH.close();
            } catch (IOException e3) {
                th = e3;
                bh.c("Error when loading resources from url: " + kY + " " + th.getMessage(), th);
                this.Zf.a(a.Yz);
                kH.close();
            }
        } else {
            this.Zf.a(a.Yy);
        }
    }

    void a(bg<j> bgVar) {
        this.Zf = bgVar;
    }

    void bJ(String str) {
        bh.v("Setting previous container version: " + str);
        this.Zh = str;
    }

    void bu(String str) {
        if (str == null) {
            this.Xg = this.Ze;
        } else {
            bh.v("Setting CTFE URL path: " + str);
            this.Xg = str;
        }
    }

    String kY() {
        String str = this.Zg.kn() + this.Xg + "&v=a65833898";
        if (!(this.Zh == null || this.Zh.trim().equals(Preconditions.EMPTY_ARGUMENTS))) {
            str = str + "&pv=" + this.Zh;
        }
        return cd.kT().kU().equals(a.YV) ? str + "&gtm_debug=x" : str;
    }

    public void run() {
        if (this.Zf == null) {
            throw new IllegalStateException("callback must be set before execute");
        }
        this.Zf.kl();
        kX();
    }
}