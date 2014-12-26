package com.google.android.gms.tagmanager;

import android.util.Log;

class x implements bi {
    private int sz;

    x() {
        this.sz = 5;
    }

    public void b(String str, Throwable th) {
        if (this.sz <= 6) {
            Log.e("GoogleTagManager", str, th);
        }
    }

    public void c(String str, Throwable th) {
        if (this.sz <= 5) {
            Log.w("GoogleTagManager", str, th);
        }
    }

    public void setLogLevel(int logLevel) {
        this.sz = logLevel;
    }

    public void v(String str) {
        if (this.sz <= 3) {
            Log.d("GoogleTagManager", str);
        }
    }

    public void w(String str) {
        if (this.sz <= 6) {
            Log.e("GoogleTagManager", str);
        }
    }

    public void x(String str) {
        if (this.sz <= 4) {
            Log.i("GoogleTagManager", str);
        }
    }

    public void y(String str) {
        if (this.sz <= 2) {
            Log.v("GoogleTagManager", str);
        }
    }

    public void z(String str) {
        if (this.sz <= 5) {
            Log.w("GoogleTagManager", str);
        }
    }
}