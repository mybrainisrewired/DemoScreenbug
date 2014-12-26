package com.google.android.gms.internal;

import android.content.Context;
import android.text.TextUtils;
import com.inmobi.commons.analytics.iat.impl.AdTrackerConstants;
import com.millennialmedia.android.MMAdView;
import java.math.BigInteger;
import java.util.Locale;

public final class dn {
    private static final Object px;
    private static String qX;

    static {
        px = new Object();
    }

    public static String b(Context context, String str, String str2) {
        String str3;
        synchronized (px) {
            if (qX == null && !TextUtils.isEmpty(str)) {
                c(context, str, str2);
            }
            str3 = qX;
        }
        return str3;
    }

    public static String bx() {
        String str;
        synchronized (px) {
            str = qX;
        }
        return str;
    }

    private static void c(Context context, String str, String str2) {
        try {
            ClassLoader classLoader = context.createPackageContext(str2, MMAdView.TRANSITION_DOWN).getClassLoader();
            Class forName = Class.forName("com.google.ads.mediation.MediationAdapter", false, classLoader);
            BigInteger bigInteger = new BigInteger(new byte[1]);
            String[] split = str.split(",");
            BigInteger bigInteger2 = bigInteger;
            int i = 0;
            while (i < split.length) {
                if (dq.a(classLoader, forName, split[i])) {
                    bigInteger2 = bigInteger2.setBit(i);
                }
                i++;
            }
            qX = String.format(Locale.US, "%X", new Object[]{bigInteger2});
        } catch (Throwable th) {
            qX = AdTrackerConstants.ERROR;
        }
    }
}