package com.google.android.gms.internal;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build.VERSION;
import android.os.SystemClock;
import com.google.android.gms.internal.cn.a;
import com.inmobi.commons.ads.cache.AdDatabaseHelper;
import com.inmobi.monetization.internal.imai.db.ClickDatabaseManager;
import com.millennialmedia.android.MMAdView;
import com.mopub.common.Preconditions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public final class cm extends a {
    private String lh;
    private Context mContext;
    private String oD;
    private ArrayList<String> oE;

    public cm(String str, ArrayList<String> arrayList, Context context, String str2) {
        this.oD = str;
        this.oE = arrayList;
        this.lh = str2;
        this.mContext = context;
    }

    private void aX() {
        try {
            this.mContext.getClassLoader().loadClass("com.google.ads.conversiontracking.IAPConversionReporter").getDeclaredMethod("reportWithProductId", new Class[]{Context.class, String.class, String.class, Boolean.TYPE}).invoke(null, new Object[]{this.mContext, this.oD, Preconditions.EMPTY_ARGUMENTS, Boolean.valueOf(true)});
        } catch (ClassNotFoundException e) {
            dw.z("Google Conversion Tracking SDK 1.2.0 or above is required to report a conversion.");
        } catch (NoSuchMethodException e2) {
            dw.z("Google Conversion Tracking SDK 1.2.0 or above is required to report a conversion.");
        } catch (Exception e3) {
            dw.c("Fail to report a conversion.", e3);
        }
    }

    protected String a(String str, HashMap<String, String> hashMap) {
        String packageName = this.mContext.getPackageName();
        String str2 = Preconditions.EMPTY_ARGUMENTS;
        try {
            String str3 = this.mContext.getPackageManager().getPackageInfo(packageName, 0).versionName;
        } catch (NameNotFoundException e) {
            dw.c("Error to retrieve app version", e);
            str3 = str2;
        }
        long elapsedRealtime = SystemClock.elapsedRealtime() - dj.bu().bw();
        Iterator it = hashMap.keySet().iterator();
        while (it.hasNext()) {
            str2 = (String) it.next();
            str = str.replaceAll(String.format("(?<!@)((?:@@)*)@%s(?<!@)((?:@@)*)@", new Object[]{str2}), String.format("$1%s$2", new Object[]{hashMap.get(str2)}));
        }
        return str.replaceAll(String.format("(?<!@)((?:@@)*)@%s(?<!@)((?:@@)*)@", new Object[]{"sessionid"}), String.format("$1%s$2", new Object[]{dj.qK})).replaceAll(String.format("(?<!@)((?:@@)*)@%s(?<!@)((?:@@)*)@", new Object[]{AdDatabaseHelper.COLUMN_APPID}), String.format("$1%s$2", new Object[]{packageName})).replaceAll(String.format("(?<!@)((?:@@)*)@%s(?<!@)((?:@@)*)@", new Object[]{"osversion"}), String.format("$1%s$2", new Object[]{String.valueOf(VERSION.SDK_INT)})).replaceAll(String.format("(?<!@)((?:@@)*)@%s(?<!@)((?:@@)*)@", new Object[]{"sdkversion"}), String.format("$1%s$2", new Object[]{this.lh})).replaceAll(String.format("(?<!@)((?:@@)*)@%s(?<!@)((?:@@)*)@", new Object[]{"appversion"}), String.format("$1%s$2", new Object[]{obj})).replaceAll(String.format("(?<!@)((?:@@)*)@%s(?<!@)((?:@@)*)@", new Object[]{ClickDatabaseManager.COLUMN_TIMESTAMP}), String.format("$1%s$2", new Object[]{String.valueOf(elapsedRealtime)})).replaceAll(String.format("(?<!@)((?:@@)*)@%s(?<!@)((?:@@)*)@", new Object[]{"[^@]+"}), String.format("$1%s$2", new Object[]{Preconditions.EMPTY_ARGUMENTS})).replaceAll("@@", "@");
    }

    public String getProductId() {
        return this.oD;
    }

    protected int j(int i) {
        return i == 0 ? 1 : i == 1 ? MMAdView.TRANSITION_UP : i == 4 ? MMAdView.TRANSITION_DOWN : 0;
    }

    public void recordPlayBillingResolution(int billingResponseCode) {
        if (billingResponseCode == 0) {
            aX();
        }
        HashMap hashMap = new HashMap();
        hashMap.put("google_play_status", String.valueOf(billingResponseCode));
        hashMap.put("sku", this.oD);
        hashMap.put("status", String.valueOf(j(billingResponseCode)));
        Iterator it = this.oE.iterator();
        while (it.hasNext()) {
            new du(this.mContext, this.lh, a((String) it.next(), hashMap)).start();
        }
    }

    public void recordResolution(int resolution) {
        if (resolution == 1) {
            aX();
        }
        HashMap hashMap = new HashMap();
        hashMap.put("status", String.valueOf(resolution));
        hashMap.put("sku", this.oD);
        Iterator it = this.oE.iterator();
        while (it.hasNext()) {
            new du(this.mContext, this.lh, a((String) it.next(), hashMap)).start();
        }
    }
}