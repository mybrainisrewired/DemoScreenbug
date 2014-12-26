package com.clmobile.app;

import com.clmobile.utils.Utils;
import com.yongding.logic.MyApplication;

public class PluginApplication extends MyApplication {
    public void onCreate() {
        super.onCreate();
        Utils.startPluginService(getApplicationContext());
    }
}