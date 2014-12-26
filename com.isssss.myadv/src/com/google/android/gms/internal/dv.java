package com.google.android.gms.internal;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings.Secure;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.ViewCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public final class dv {
    public static final Handler rp;

    static {
        rp = new Handler(Looper.getMainLooper());
    }

    public static int a(Context context, int i) {
        return a(context.getResources().getDisplayMetrics(), i);
    }

    public static int a(DisplayMetrics displayMetrics, int i) {
        return (int) TypedValue.applyDimension(1, (float) i, displayMetrics);
    }

    public static void a(ViewGroup viewGroup, ak akVar, String str) {
        a(viewGroup, akVar, str, ViewCompat.MEASURED_STATE_MASK, -1);
    }

    private static void a(ViewGroup viewGroup, ak akVar, String str, int i, int i2) {
        if (viewGroup.getChildCount() == 0) {
            Context context = viewGroup.getContext();
            View textView = new TextView(context);
            textView.setGravity(ApiEventType.API_MRAID_GET_SCREEN_SIZE);
            textView.setText(str);
            textView.setTextColor(i);
            textView.setBackgroundColor(i2);
            View frameLayout = new FrameLayout(context);
            frameLayout.setBackgroundColor(i);
            int a = a(context, (int)MMAdView.TRANSITION_DOWN);
            frameLayout.addView(textView, new LayoutParams(akVar.widthPixels - a, akVar.heightPixels - a, 17));
            viewGroup.addView(frameLayout, akVar.widthPixels, akVar.heightPixels);
        }
    }

    public static void a(ViewGroup viewGroup, ak akVar, String str, String str2) {
        dw.z(str2);
        a(viewGroup, akVar, str, SupportMenu.CATEGORY_MASK, ViewCompat.MEASURED_STATE_MASK);
    }

    public static boolean bC() {
        return Build.DEVICE.startsWith("generic");
    }

    public static boolean bD() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static String l(Context context) {
        String string = Secure.getString(context.getContentResolver(), "android_id");
        if (string == null || bC()) {
            string = "emulator";
        }
        return u(string);
    }

    public static String u(String str) {
        int i = 0;
        while (i < 2) {
            try {
                MessageDigest.getInstance("MD5").update(str.getBytes());
                return String.format(Locale.US, "%032X", new Object[]{new BigInteger(1, messageDigest.digest())});
            } catch (NoSuchAlgorithmException e) {
                i++;
            }
        }
        return null;
    }
}