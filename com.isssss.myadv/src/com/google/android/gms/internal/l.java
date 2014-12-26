package com.google.android.gms.internal;

import android.content.Context;
import android.net.Uri;
import android.view.MotionEvent;

public class l {
    private String kd;
    private String ke;
    private String[] kf;
    private h kg;
    private final g kh;

    public l(h hVar) {
        this.kd = "googleads.g.doubleclick.net";
        this.ke = "/pagead/ads";
        this.kf = new String[]{".doubleclick.net", ".googleadservices.com", ".googlesyndication.com"};
        this.kh = new g();
        this.kg = hVar;
    }

    private Uri a(Uri uri, Context context, String str, boolean z) throws m {
        try {
            if (uri.getQueryParameter("ms") != null) {
                throw new m("Query parameter already exists: ms");
            }
            return a(uri, "ms", z ? this.kg.a(context, str) : this.kg.a(context));
        } catch (UnsupportedOperationException e) {
            throw new m("Provided Uri is not in a valid state");
        }
    }

    private Uri a(Uri uri, String str, String str2) throws UnsupportedOperationException {
        String toString = uri.toString();
        int indexOf = toString.indexOf("&adurl");
        if (indexOf == -1) {
            indexOf = toString.indexOf("?adurl");
        }
        return indexOf != -1 ? Uri.parse(new StringBuilder(toString.substring(0, indexOf + 1)).append(str).append("=").append(str2).append("&").append(toString.substring(indexOf + 1)).toString()) : uri.buildUpon().appendQueryParameter(str, str2).build();
    }

    public Uri a(Uri uri, Context context) throws m {
        try {
            return a(uri, context, uri.getQueryParameter("ai"), true);
        } catch (UnsupportedOperationException e) {
            throw new m("Provided Uri is not in a valid state");
        }
    }

    public void a(MotionEvent motionEvent) {
        this.kg.a(motionEvent);
    }

    public boolean a(Uri uri) {
        if (uri == null) {
            throw new NullPointerException();
        }
        try {
            String host = uri.getHost();
            String[] strArr = this.kf;
            int length = strArr.length;
            int i = 0;
            while (i < length) {
                if (host.endsWith(strArr[i])) {
                    return true;
                }
                i++;
            }
            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }

    public h y() {
        return this.kg;
    }
}