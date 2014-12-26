package com.google.android.gms.analytics;

import android.util.Log;

class l implements Logger {
    private int sz;

    l() {
        this.sz = 1;
    }

    private String E(String str) {
        return Thread.currentThread().toString() + ": " + str;
    }

    public void error(Exception exception) {
        if (this.sz <= 3) {
            Log.e("GAV4", null, exception);
        }
    }

    public void error(String msg) {
        if (this.sz <= 3) {
            Log.e("GAV4", E(msg));
        }
    }

    public int getLogLevel() {
        return this.sz;
    }

    public void info(String msg) {
        if (this.sz <= 1) {
            Log.i("GAV4", E(msg));
        }
    }

    public void setLogLevel(int level) {
        this.sz = level;
    }

    public void verbose(String msg) {
        if (this.sz <= 0) {
            Log.v("GAV4", E(msg));
        }
    }

    public void warn(String msg) {
        if (this.sz <= 2) {
            Log.w("GAV4", E(msg));
        }
    }
}