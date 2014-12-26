package com.google.android.gms.internal;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import com.google.android.gms.cast.Cast;
import com.inmobi.monetization.internal.InvalidManifestConfigException;
import com.millennialmedia.android.MMAdView;
import java.util.Locale;

public final class dg {
    public final int pZ;
    public final boolean qa;
    public final boolean qb;
    public final String qc;
    public final String qd;
    public final boolean qe;
    public final boolean qf;
    public final boolean qg;
    public final String qh;
    public final String qi;
    public final int qj;
    public final int qk;
    public final int ql;
    public final int qm;
    public final int qn;
    public final int qo;
    public final float qp;
    public final int qq;
    public final int qr;

    public dg(Context context) {
        boolean z = true;
        AudioManager audioManager = (AudioManager) context.getSystemService("audio");
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        Locale locale = Locale.getDefault();
        PackageManager packageManager = context.getPackageManager();
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        this.pZ = audioManager.getMode();
        this.qa = a(packageManager, "geo:0,0?q=donuts") != null;
        if (a(packageManager, "http://www.google.com") == null) {
            z = false;
        }
        this.qb = z;
        this.qc = telephonyManager.getNetworkOperator();
        this.qd = locale.getCountry();
        this.qe = dv.bC();
        this.qf = audioManager.isMusicActive();
        this.qg = audioManager.isSpeakerphoneOn();
        this.qh = locale.getLanguage();
        this.qi = a(packageManager);
        this.qj = audioManager.getStreamVolume(MMAdView.TRANSITION_DOWN);
        this.qk = a(context, connectivityManager, packageManager);
        this.ql = telephonyManager.getNetworkType();
        this.qm = telephonyManager.getPhoneType();
        this.qn = audioManager.getRingerMode();
        this.qo = audioManager.getStreamVolume(MMAdView.TRANSITION_UP);
        this.qp = displayMetrics.density;
        this.qq = displayMetrics.widthPixels;
        this.qr = displayMetrics.heightPixels;
    }

    private static int a(Context context, ConnectivityManager connectivityManager, PackageManager packageManager) {
        if (!dq.a(packageManager, context.getPackageName(), "android.permission.ACCESS_NETWORK_STATE")) {
            return InvalidManifestConfigException.MISSING_ACTIVITY_DECLARATION;
        }
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null ? activeNetworkInfo.getType() : -1;
    }

    private static ResolveInfo a(PackageManager packageManager, String str) {
        return packageManager.resolveActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)), Cast.MAX_MESSAGE_LENGTH);
    }

    private static String a(PackageManager packageManager) {
        ResolveInfo a = a(packageManager, "market://details?id=com.google.android.gms.ads");
        if (a == null) {
            return null;
        }
        ActivityInfo activityInfo = a.activityInfo;
        if (activityInfo == null) {
            return null;
        }
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(activityInfo.packageName, 0);
            return packageInfo != null ? packageInfo.versionCode + "." + activityInfo.packageName : null;
        } catch (NameNotFoundException e) {
            return null;
        }
    }
}