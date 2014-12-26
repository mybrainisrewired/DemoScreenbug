package com.google.android.gms.tagmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import com.inmobi.commons.analytics.iat.impl.AdTrackerConstants;
import com.mopub.common.Preconditions;
import java.util.HashMap;
import java.util.Map;

class ay {
    private static String Yk;
    static Map<String, String> Yl;

    static {
        Yl = new HashMap();
    }

    ay() {
    }

    static void bF(String str) {
        synchronized (ay.class) {
            Yk = str;
        }
    }

    static void c(Context context, String str) {
        cy.a(context, "gtm_install_referrer", AdTrackerConstants.REFERRER, str);
        e(context, str);
    }

    static String d(Context context, String str) {
        if (Yk == null) {
            synchronized (ay.class) {
                if (Yk == null) {
                    SharedPreferences sharedPreferences = context.getSharedPreferences("gtm_install_referrer", 0);
                    if (sharedPreferences != null) {
                        Yk = sharedPreferences.getString(AdTrackerConstants.REFERRER, Preconditions.EMPTY_ARGUMENTS);
                    } else {
                        Yk = Preconditions.EMPTY_ARGUMENTS;
                    }
                }
            }
        }
        return m(Yk, str);
    }

    static String e(Context context, String str, String str2) {
        String str3 = (String) Yl.get(str);
        if (str3 == null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("gtm_click_referrers", 0);
            str3 = sharedPreferences != null ? sharedPreferences.getString(str, Preconditions.EMPTY_ARGUMENTS) : Preconditions.EMPTY_ARGUMENTS;
            Yl.put(str, str3);
        }
        return m(str3, str2);
    }

    static void e(Context context, String str) {
        String m = m(str, "conv");
        if (m != null && m.length() > 0) {
            Yl.put(m, str);
            cy.a(context, "gtm_click_referrers", m, str);
        }
    }

    static String m(String str, String str2) {
        return str2 == null ? str.length() > 0 ? str : null : Uri.parse("http://hostname/?" + str).getQueryParameter(str2);
    }
}