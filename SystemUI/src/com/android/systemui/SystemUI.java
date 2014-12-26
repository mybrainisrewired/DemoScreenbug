package com.android.systemui;

import android.content.Context;
import android.content.res.Configuration;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public abstract class SystemUI {
    public Context mContext;

    public void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
    }

    protected void onConfigurationChanged(Configuration newConfig) {
    }

    public abstract void start();
}