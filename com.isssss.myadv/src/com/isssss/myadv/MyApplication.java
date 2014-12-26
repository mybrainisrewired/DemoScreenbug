package com.isssss.myadv;

import android.app.Application;
import com.millennialmedia.android.MMSDK;

public class MyApplication extends Application {
    public void onCreate() {
        super.onCreate();
        MMSDK.initialize(this);
    }
}